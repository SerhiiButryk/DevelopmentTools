from datetime import datetime
import os
import re

from bs4 import BeautifulSoup
from exa_py import Exa
from exa_py.api import SearchResponse
import requests

from utils import log, log_to_file
from curl_cffi import requests

from exa_py import Exa

request_timeout = 10  # seconds

headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
}

# Extract URLs from the response text using regex
# urls = re.findall(
#         r'https?://[^\s<>"\'()]+',
#         response
#     )

def basic_search(
        query: str, 
        include: list[str] = [], 
        exclude: list[str] = [], 
        domain: str = "", 
        debug: bool = True,
    ) -> str:

    """ 
    Example:
    basic_search(query="android", include=["developer"], domain="eleks.com") 
    """


    duckduckgo_url = "https://html.duckduckgo.com/html/"

    query = f"\"{query}\" "

    for item in include:
        query += f"+{item} "

    for item in exclude:
        query += f"-{item} "    

    if domain:
        query += f"site:{domain} "           

    data = {
        "q": query,
        "b": "",
        # Other params...
    }

    response = requests.post(
        url=duckduckgo_url, 
        data=data, 
        headers=headers, 
        timeout=request_timeout,
        impersonate="safari15_5",
    )

    log(message=f"Got response: '{response.status_code}', query='{query}', size = '{len(response.text)}'")

    if debug:
        timestamp = datetime.now().strftime("%Y-%m-%d_%H:%M:%S")
        fileName = f"search_results_{timestamp}.html"
        log_to_file(message=response.text, filename=fileName, override=True)

    return response.text

def get_content(url: str, debug: bool = False, butify: bool = False) -> str:

    response = requests.get(
        url=url, 
        timeout=request_timeout, 
        headers=headers, 
        impersonate="safari15_5"
    )

    log(message=f"Got response: '{response.status_code}', size = '{len(response.text)}'")

    if butify:

        try:        
        
            log(f" Beautifying response from {url}")
            
            soup = BeautifulSoup(response.text, "html.parser")
            text = soup.get_text(separator=" ", strip=True)

            # Normalize whitespace
            text = re.sub(r'\s+', ' ', text)   

            # Keep snippet sizes safe
            result = text   

            if debug:
                log_to_file(message=result, filename="get_resource_results.txt", override=True)

            log(f" Done beautifying response from {url}, size = '{len(result)}'")    

            return result

        except Exception as e:
            log(f"Error occurred while beautifying response from {url}: {e}")
    
    if debug:
        log_to_file(message=response.text, filename="get_resource_results.html", override=True)

    return response.text    

def get_content_using_exa(url: str, summary: bool = False, highlights: bool = False, fullContent: bool = True) -> None:

    exa = Exa(api_key=os.getenv("EXA_API_KEY"))

    if fullContent:

        response: SearchResponse = exa.get_contents(
            [url],
            text=True
        )

        # response.results[0] is Result object,
        # doc ref: https://exa.ai/docs/sdks/python-sdk-specification#result
        log(message=f"Response size: {len(response.results[0].text)}")
        return response.results[0].text

    elif summary:

        response: SearchResponse = exa.get_contents(
            [url],
            summary=True
        )

        # response.results[0] is Result object,
        # doc ref: https://exa.ai/docs/sdks/python-sdk-specification#result
        log(message=f"Response size: {len(response.results[0].summary)}")
        return response.results[0].summary
            
    elif highlights:

        response: SearchResponse = exa.get_contents(
            [url],
            highlights=True
        )

        # response.results[0] is Result object,
        # doc ref: https://exa.ai/docs/sdks/python-sdk-specification#result
        log(message=f"Response size: {len(response.results[0].highlights)}")
        return response.results[0].highlights

    else:

        mes = "No content type specified. Please specify one of the following: summary, highlights, or fullContent."
        log(message=mes)
        return ""