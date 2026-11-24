from pydantic import BaseModel

# Define the Response format for a found vacancy 

class LeadResponse(BaseModel):
    company: str
    contact_info: str
    email: str
    summary: str
    outreach_message: str
    url: str
    tools_used: list[str]

class LeadResponseList(BaseModel):
    leads: list[LeadResponse]

# Define the Response format for a found company

class CompanyResponse(BaseModel): 
    company: str   

class CompaniesList(BaseModel):
    list: list[CompanyResponse]   

# Other formats

class EmptyResponse(BaseModel):
    message: str = ""  