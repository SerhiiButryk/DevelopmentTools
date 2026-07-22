"""
    Tools for the AI agent.
"""

from datetime import datetime
import os
from bs4 import BeautifulSoup
import re

from langchain_core.tools import tool
from langchain_exa import ExaSearchResults

from net import basic_search, get_resource
from utils import log, log_to_file

@tool
def search_tool(query: str) -> str:
    """Search the web using a search engine to gather raw text information on any topic or company."""

    debug = False

    # Should be only one log entry per function call
    log(message = f"Called with arg: {query}", count = True)

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
def scrape_tool(company_name: str) -> str:
    """Scrapes local web data and searches for background info regarding a specific company name to find potential IT needs."""

    # Should be only one log entry per function call
    log(message = f"Called with arg: {company_name}", count = True)

    results = []

    response: str = basic_search(company_name)

    urls = re.findall(
        r'https?://[^\s<>"\'()]+',
        response
    )

    unique_urls = set(urls)

    if unique_urls:

        log(f"Found URLs = {len(unique_urls)} for '{company_name}'")


        for url in unique_urls:

            try:        

                log(f"Visiting {url}")

                response = get_resource(url)
                
                soup = BeautifulSoup(response, "html.parser")
                text = soup.get_text(separator=" ", strip=True)

                # Normalize whitespace
                text = re.sub(r'\s+', ' ', text)   

                # Keep snippet sizes safe
                results.append(text[:500])         

            except Exception as e:
                log(f"Error occurred while scraping {url}: {e}")

    else:
        log(f"No URLs found for '{company_name}'")            

    if results:

        log(f"Success!")

        return "\n\n".join(results)
    
    else:

        log(f"Failed!")

        return "An error occurred while using this tool."

@tool
def save_tool(data: str, filename: str = "leads_output.txt") -> str:
    """Saves the final compiled structured JSON string or text data directly to a local text file repository."""

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    formatted_text = f"--- Leads Output ---\nTimestamp: {timestamp}\n\n{data}\n\n"

    log_to_file(message=formatted_text, filename=filename)
    
    return f"Data successfully saved to {filename}"