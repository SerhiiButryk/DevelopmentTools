
import asyncio

from data_formats import CompaniesList, LeadResponseList
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

        log("handleNewMessage() Started working...")

        resp = self.agent.runQuery(
            user_query="Get me the company list from 'SKILL.md' file", 
            system_prompt="",
            response_format=CompaniesList,
            )

        stop_server = False

        if (isinstance(resp, CompaniesList)):
            
            log(f"handleNewMessage() Companies found = {len(resp.list)}")   
            
            in_message = AIMessage()
            in_message.ParseFromString(raw_bytes)

            out_message = AIMessage() 
            out_message.userquery = ""
            out_message.systemprompt = ""

            for item in resp.list:

                log(f"handleNewMessage() Looking for company '{item.company}'...") 

                resp = self.agent.runQuery(
                    user_query="Find Android developer job vacancies in Ukraine for company '" + item.company + "'", 
                    system_prompt=in_message.systemprompt,
                    response_format=LeadResponseList,
                    )

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

            log(f"handleNewMessage() Stopping server...") 
        
            # Close WebSocket client
            self.server.stop()
            await self.server_task

        else:

            log(f"handleNewMessage() No response") 

            out_message = AIMessage() 
            return await websocket.send(out_message.SerializeToString())              

