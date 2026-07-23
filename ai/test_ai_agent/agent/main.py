"""
    Main AI agent script.

    Langchain docs: https://reference.langchain.com/python/langgraph
"""

import asyncio
import os
import ssl

# Load environment variables from a .env file.
from dotenv import load_dotenv

from client_handler import ClientHandler
from net.net import get_content_using_exa
from net.websockets import create_websocket_server_and_listen
from utils import init_log

handler = ClientHandler()

async def main():

    global handler

    print(f"Working...")

    # Globally disable SSL certificate verification checks
    try:
        _create_unverified_https_context = ssl._create_unverified_context
    except AttributeError:
        pass
    else:
        ssl._create_default_https_context = _create_unverified_https_context

    # Force the underlying HTTP network engine to bypass SSL strict verification
    os.environ["PYTHONHTTPSVERIFY"] = "0"

    # Load environment variables from a .env file
    load_dotenv()

    init_log()

    handler.init()

    async def handle_client_message(websocket, raw_bytes: bytes):
        await handler.handleNewMessage(websocket, raw_bytes)

    await create_websocket_server_and_listen(handle_client_message=handle_client_message)

if __name__ == "__main__":
    asyncio.run(main())