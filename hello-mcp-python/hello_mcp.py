from mcp.server import FastMCP
import logging
import requests
import os
import json

# Jenkins configuration (global variables)
JENKINS_JOB_NAME = None
JENKINS_URL = None
JENKINS_USERNAME = None
JENKINS_API_TOKEN = None

# Load Jenkins config from local config file
config_path = os.path.join(os.path.dirname(__file__), "jenkins_config.json")
try:
    with open(config_path, "r") as f:
        config = json.load(f)
        JENKINS_JOB_NAME = config.get("job_name")
        JENKINS_URL = config.get("url")
        JENKINS_USERNAME = config.get("username")
        JENKINS_API_TOKEN = config.get("token")
except Exception as e:
    logging.error(f"Could not load Jenkins config from {config_path}: {e}")
    JENKINS_JOB_NAME = None
    JENKINS_URL = None
    JENKINS_USERNAME = None
    JENKINS_API_TOKEN = None

# Create MCP server
mcp = FastMCP(name="hello-mcp")

# Simple tool
@mcp.tool()
def sayHello(name: str) -> dict:
    """Returns a hello message."""
    logging.info("Processing request")
    return {"message": f"Hello SRK, {name}!"}

# Jenkins build status tool

@mcp.tool()
def getJenkinsStatus() :
    """Fetches the latest Jenkins build status for the given job URL, with optional authentication."""
    if not JENKINS_URL or not JENKINS_JOB_NAME:
        return {"error": "JENKINS_URL or JENKINS_JOB_NAME not set", "url": JENKINS_URL}
    api_url = JENKINS_URL.rstrip('/') + f'/job/{JENKINS_JOB_NAME}/lastBuild/api/json'
    auth = (JENKINS_USERNAME, JENKINS_API_TOKEN) if JENKINS_USERNAME and JENKINS_API_TOKEN else None
    try:
        response = requests.get(api_url, timeout=10, auth=auth)
        response.raise_for_status()
        data = response.json()
        status = data.get('result', 'UNKNOWN')
        build_number = data.get('number', 'N/A')
        return {
            "build_number": build_number,
            "status": status,
            "url": JENKINS_URL
        }
    except Exception as e:
        logging.error(f"Failed to fetch Jenkins status: {e}")
        return {"error": str(e), "url": JENKINS_URL}
        
# Start Jenkins build tool
@mcp.tool()
def startJenkinsBuild() -> dict:
    """Starts a new Jenkins build for the given job URL with authentication."""
    if not JENKINS_URL or not JENKINS_JOB_NAME:
        return {"error": "JENKINS_URL or JENKINS_JOB_NAME not set", "url": JENKINS_URL}
    url = JENKINS_URL.rstrip('/') + f'/job/{JENKINS_JOB_NAME}/build'
    auth = (JENKINS_USERNAME, JENKINS_API_TOKEN) if JENKINS_USERNAME and JENKINS_API_TOKEN else None
    try:
        response = requests.post(url, timeout=10, auth=auth)
        response.raise_for_status()
        return {"message": "Build triggered successfully.", "url": url, "status_code": response.status_code}
    except Exception as e:
        logging.error(f"Failed to start Jenkins build: {e}")
        return {"error": str(e), "url": url}

@mcp.prompt("commitAndPush")
def hello_world(name: str = "World") -> str:
    """commit and push the changes"""
    return f"commit and push the changes"

# Run the MCP server
mcp.run()
