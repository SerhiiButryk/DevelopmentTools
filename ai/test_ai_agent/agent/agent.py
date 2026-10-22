import os

from langchain_google_genai import ChatGoogleGenerativeAI
from langchain.agents import create_agent
from pydantic import BaseModel
from deepagents import create_deep_agent
from deepagents.backends.filesystem import FilesystemBackend
from langchain_deepseek import ChatDeepSeek

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

    def selectAgent(self):

        model_name = os.getenv("MODEL_NAME")

        if model_name.startswith("gemini"):
                
            llm = ChatGoogleGenerativeAI(   
                model=model_name
            )

            log(f"Gemini model selected: {model_name}")   

            return llm

        if model_name.startswith("deepseek"):

            llm = ChatDeepSeek(
                model=model_name,
                temperature=0,
                max_tokens=None,
                timeout=None,
                max_retries=3,
            )

            log(f"DeepSeek model selected: {model_name}")

            return llm

        raise ValueError(f"Unsupported model: {model_name}. Please set MODEL_NAME to a valid model name.")


    def runQuery(self, user_query, system_prompt, debug = True) -> LeadResponseList:

        log(f"Running query... size = '{len(user_query)}', system prompt size = '{len(system_prompt)}'")   

        llm = self.selectAgent()

        # List the tools from our tools.py file
        tools = [raw_search_tool, search_tool, scraping_tool]

        # Lifecycle of the agent:
        #
        #                                                          | -> if 'tool_calls' is in the AI response -> call the tool
        # Loading SKILLs --> Apply system prompt -> Call model ->  | 
        #                                                          | -> if 'tool_calls' is not in the AI response -> stop and return the response
        #
        # Responses:
        # 'HumanMessage' - a message from the user
        # 'AIMessage' - a response from AI model
        # 'ToolMessage' - a response from a tool

        backend = FilesystemBackend(root_dir="./")

        self.agent = create_deep_agent(
            model=llm,
            system_prompt=system_prompt,
            tools=tools,
            response_format=LeadResponseList,
            skills=["./skills/"],
            backend=backend,
        )

        # Run the agent using the correct 'messages' list format required by modern runtimes
        raw_response = self.agent.invoke({"messages": [("user", user_query)]})
    
        if "messages" in raw_response:

            # OR if 'response_format' is not set
            # response = raw_response['messages'][-1].content
            response = raw_response["structured_response"]

            if debug:
                log(f"Received a valid response, \n response = '{response}' \n") 
            else:
                log(f"Received a valid response")         

            return response

        else:

            log(f"Received an EMPTY response")
            
            return LeadResponseList()