"""
    Tools for the AI agent.
"""

from datetime import datetime
import os

from langchain_core.tools import tool
from langchain_exa import ExaSearchResults

from net.net import basic_search, get_content_using_exa
from utils import log, log_to_file

@tool
def search_tool(query: str) -> str:
    """Search the web using a search engine to gather raw text information on any topic or company."""

    debug = False

    # Should be only one log entry per function call
    log(message = f"Called with args: '{query}'", count = True)

    # Initialize the ExaSearchResults tool
    search_tool = ExaSearchResults(exa_api_key=os.getenv("EXA_API_KEY"))

    # Perform a search query
    search_results = search_tool._run(
        query=query,
        num_results=5,
        text_contents_options=True,
        highlights=True,
    )

    if debug:
        log(message=f"Search results: '{search_results}'")

    return search_results

@tool
def raw_search_tool(query: str, domain: str) -> str:
    """Search the web using a search engine to gather raw text information for the given query and domain."""

    # Should be only one log entry per function call
    log(message = f"Called with args: '{query}', '{domain}'", count = True)

    response: str = basic_search(query=query, domain=domain)
    return response

@tool
def scraping_tool(url: str, summary: bool = False, highlights: bool = False) -> str:
    """Scrape the content of a web page and return the text content. Optionally, set 'summary' or 'highlights' to True to get a summary or highlights of the content."""

    # Should be only one log entry per function call
    log(message = f"Called with args: '{url}', 'summary': '{summary}', 'highlights': '{highlights}'", count = True)

    setFullContent = not (summary or highlights)

    response: str = get_content_using_exa(url=url, summary=summary, highlights=highlights, fullContent=setFullContent)
    return response

@tool
def save_tool(data: str, filename: str = "ai_output.txt") -> str:
    """Save the given data to a file with a timestamp for reference."""

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    formatted_text = f"--- AI Output ---\nTimestamp: {timestamp}\n\n{data}\n\n"

    log_to_file(message=formatted_text, filename=filename)
    
    return f"Data successfully saved to '{filename}'"