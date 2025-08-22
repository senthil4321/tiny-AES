from mcp.server import FastMCP
import logging

# Create MCP server
mcp = FastMCP(name="hello-mcp")

# Simple tool
@mcp.tool()
def say_hello(name: str) -> dict:
    """Returns a hello message."""
    logging.info("Processing request")
    return {"message": f"Hello, {name}!"}

# Run the MCP server
mcp.run()
