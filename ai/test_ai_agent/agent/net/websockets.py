import asyncio
import os
from utils import *

from websockets.asyncio.server import serve

async def create_websocket_server_and_listen(handle_client_message=None):

    """Creates a WebSocket server and listens for incoming messages."""

    global server, server_task

    host = os.getenv("WEBUI_HOST")
    port = int(os.getenv("WEBUI_PORT"))

    server = WebSocketServer(host, port, on_message=handle_client_message)
    
    server_task = asyncio.create_task(server.start())

    # Wait for the server to finish (it will run until stopped)
    await server_task  

class WebSocketServer:

    """A simple WebSocket server."""

    def __init__(self, host, port, on_message=None):
        self.host = host
        self.port = port
        self.stop_event = asyncio.Event()
        self.connected_clients = set()
        self.on_message = on_message  # Callback for handling messages

    async def start(self):

        async with serve(self.handle_client, self.host, self.port):
            log(f"WebSocket server running on ws://{self.host}:{self.port}")
            # Wait until stop_event is set instead of hanging forever
            await self.stop_event.wait()
        
        # Once stop_event is set, the async with block exits and closes all connections cleanly
        log("WebSocket server stopped cleanly.")

    def stop(self):
        """Call this method to initiate graceful shutdown."""

        self.stop_event.set() 

    async def send_message(self, websocket, message: str):
        """Send a message to a specific connected client."""

        if websocket in self.connected_clients:
            await websocket.send(message)

    async def handle_client(self, websocket):

        self.connected_clients.add(websocket)

        log("Client connected!")

        async for message in websocket:

            log(f"Received: a message")

            if self.on_message:
                await self.on_message(websocket, message)
