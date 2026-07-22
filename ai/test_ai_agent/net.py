import requests

from utils import log, log_to_file

request_timeout = 10  # seconds

def basic_search(query: str, debug: bool = False) -> str:

    duckduckgo_url = "https://html.duckduckgo.com/html/"

    headers = {
        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    }

    data = {
        "q": query
        # Other params...
    }


    response = requests.post(url=duckduckgo_url, data=data, headers=headers, timeout=request_timeout)

    log(message=f"Got response: '{response.status_code}', query='{query}', size = '{len(response.text)}'")

    if debug:
        log_to_file(message=response.text, filename="search_results.html")

    return response.text

def get_resource(url: str, debug: bool = False) -> str:

    response = requests.get(url, timeout=request_timeout)

    log(message=f"Got response: '{response.status_code}', size = '{len(response.text)}'")
    
    if debug:
        log_to_file(message=response.text, filename="get_resource_results.html")

    return response.text    