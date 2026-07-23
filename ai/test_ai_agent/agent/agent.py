import os

from langchain_google_genai import ChatGoogleGenerativeAI
from langchain.agents import create_agent
from pydantic import BaseModel

from tools import raw_search_tool, scraping_tool, search_tool  

from utils import log

# Define the structure of each lead in the output
class LeadResponse(BaseModel):
    company: str
    contact_info: str
    email: str
    summary: str
    outreach_message: str
    url: str
    tools_used: list[str]

# Define a list structure to hold multiple leads
class LeadResponseList(BaseModel):
    leads: list[LeadResponse]

class Agent:

    """Class to encapsulate the AI agent and its configuration."""

    def runQuery(self, user_query, system_prompt) -> LeadResponseList:

        log(f"Run query size = : '{len(user_query)}")  
        log(f"AND system prompt size = : '{len(system_prompt)}'")      

        model_name = os.getenv("MODEL_NAME")
        
        # Determining which AI model we will use
        llm = ChatGoogleGenerativeAI(   
            model=model_name
        )

        # List the tools from our tools.py file
        tools = [raw_search_tool, search_tool, scraping_tool]

        # Lifecycle of the agent:
        #
        #                                       | -> if 'tool_calls' is in the AI response -> call the tool
        # Apply system prompt -> Call model ->  | 
        #                                       | -> if 'tool_calls' is not in the AI response -> stop and return the response
        #
        # Responses:
        # 'HumanMessage' - a message from the user
        # 'AIMessage' - a response from AI model
        # 'ToolMessage' - a response from a tool

        self.agent = create_agent(
            model=llm,
            system_prompt=system_prompt,
            tools=tools,
            response_format=LeadResponseList,
        )

        # Run the agent using the correct 'messages' list format required by modern runtimes
        raw_response = self.agent.invoke({"messages": [("user", user_query)]})

        log(f"Received raw response from ai")  
    
        if "messages" in raw_response:

            # OR if 'response_format' is not set
            # response = raw_response['messages'][-1].content
            response = raw_response["structured_response"]

            log(f"Final answer: type = '{type(response)}' , response = '{response}'")  

            return response

        else:
            
            return LeadResponseList()