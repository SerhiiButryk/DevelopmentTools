
import asyncio

from proto.messages_pb2 import AIMessage, Company, Keywords

from agent import Agent
from net.websockets import WebSocketServer
from utils import log

class ClientHandler:

    server: WebSocketServer = None
    server_task: asyncio.Task = None

    agent: Agent = None

    def init(self):
        self.agent = Agent()

    async def handleNewMessage(self, websocket, raw_bytes: bytes):

        log(f"Received a new message")

        stop_server = False

        in_message = AIMessage()
        in_message.ParseFromString(raw_bytes)

        if in_message.userquery: 

            resp = self.agent.runQuery(user_query=in_message.userquery, system_prompt=in_message.systemprompt)
            
            out_message = AIMessage() 
            out_message.userquery = ""
            out_message.systemprompt = ""

            for lead in resp.leads:

                company = Company()
                company.name = lead.company
                company.contact_info = lead.contact_info
                company.summary = lead.summary
                company.email = lead.email
                company.outreach_message = lead.outreach_message
                company.url = lead.url

                for tool in lead.tools_used:
                
                    keyword = Keywords()
                    keyword.name = tool

                    company.keywords.append(keyword)

                out_message.items.append(company)

            log(f"Sending response, item count = {len(resp.leads)}")    

            await websocket.send(out_message.SerializeToString())    

        elif stop_server: 

            # Close WebSocket client
            self.server.stop()
            await self.server_task

        else:

            log("Nothing to process")                

