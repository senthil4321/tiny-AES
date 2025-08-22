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
def say_hello(name: str) -> dict:
    """Returns a hello message."""
    logging.info("Processing request")
    return {"message": f"Hello SRK, {name}!"}

# Jenkins build status tool

@mcp.tool()
def get_jenkins_status() :
    """Fetches the latest Jenkins build status for the given job URL, with optional authentication."""
    api_url = JENKINS_URL.rstrip('/') + '/lastBuild/api/json'
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
def start_jenkins_build() -> dict:
    """Starts a new Jenkins build for the given job URL with authentication."""
    url = JENKINS_URL.rstrip('/') + '/build'
    auth = (JENKINS_USERNAME, JENKINS_API_TOKEN) if JENKINS_USERNAME and JENKINS_API_TOKEN else None
    try:
        response = requests.post(url, timeout=10, auth=auth)
        response.raise_for_status()
        return {"message": "Build triggered successfully.", "url": url, "status_code": response.status_code}
    except Exception as e:
        logging.error(f"Failed to start Jenkins build: {e}")
        return {"error": str(e), "url": url}
# Run the MCP server
mcp.run()
