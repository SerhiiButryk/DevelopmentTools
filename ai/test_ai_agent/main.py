"""
    Main AI agent script.

    Langchain docs: https://reference.langchain.com/python/langgraph
"""

import os
import ssl
import time

# Load environment variables from a .env file.
from dotenv import load_dotenv

# Define structured output models using Pydantic
from langchain.tools import tool
from pydantic import BaseModel

# Langchain imports that we will use to interact with Gemini
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain.agents import create_agent

# Custom tools that we will use. These are pulled from our tools.py
from tools import scrape_tool, search_tool, save_tool  

from utils import init_log, log

from input_data import system_prompt, user_query

# Globally disable SSL certificate verification checks
try:
    _create_unverified_https_context = ssl._create_unverified_context
except AttributeError:
    pass
else:
    ssl._create_default_https_context = _create_unverified_https_context

# Force the underlying HTTP network engine to bypass SSL strict verification
os.environ["PYTHONHTTPSVERIFY"] = "0"

# Measure the time taken for the agent to process the request
start_time = time.perf_counter()

# Load environment variables from a .env file
load_dotenv()

init_log()

model_name = os.getenv("MODEL_NAME")

# Define the structure of each lead in the output
class LeadResponse(BaseModel):
    company: str
    contact_info: str
    email: str
    summary: str
    outreach_message: str
    tools_used: list[str]

# Define a list structure to hold multiple leads
class LeadResponseList(BaseModel):
    leads: list[LeadResponse]

# Determining which AI model we will use
llm = ChatGoogleGenerativeAI(   
    model=model_name
)

# List the tools from our tools.py file
tools = [scrape_tool, search_tool, save_tool]

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

print(f"Working...")

agent = create_agent(
    model=llm,
    system_prompt=system_prompt,
    tools=tools
)

# Run the agent using the correct 'messages' list format required by modern runtimes
raw_response = agent.invoke({"messages": [("user", user_query)]})

end_time = time.perf_counter()

taken_time = int(end_time - start_time)

log(f"Raw agent response (Time - {taken_time} sec.): {raw_response}")

if "messages" in raw_response:

    log(f"Final answer: {raw_response['messages'][-1].content}")

print(f"Finished. Time - {taken_time} sec.")