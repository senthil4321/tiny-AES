
from mcp.server import FastMCP
import logging
import requests

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
    url = "http://172.27.192.196:8080/job/srk-freestyle/"
    username = "srkjenkins"
    api_token = "11ca8af20c12a97da70b9470bc4146e1c8"
    """Fetches the latest Jenkins build status for the given job URL, with optional authentication."""
    api_url = url.rstrip('/') + '/lastBuild/api/json'
    auth = (username, api_token) if username and api_token else None
    try:
        response = requests.get(api_url, timeout=10, auth=auth)
        response.raise_for_status()
        data = response.json()
        status = data.get('result', 'UNKNOWN')
        build_number = data.get('number', 'N/A')
        return {
            "build_number": build_number,
            "status": status,
            "url": url
        }
    except Exception as e:
        logging.error(f"Failed to fetch Jenkins status: {e}")
        return {"error": str(e), "url": url}

# Run the MCP server
mcp.run()
