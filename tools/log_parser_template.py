#!/usr/bin/env python3

import sys
import re
import subprocess, os, platform

# 
# Simple template for log parsing with html report
# [Desc here]
#

# [General regex patterns]

pattern_timestamp = "(^\\d+-\\d+\\s+\\d+:\\d+:\\d+\\.\\d+\\s+\\d+\\s+\\d+)"

def Red(mes): return "\033[91m{}\033[00m" .format(mes)
def Green(mes): return "\033[92m{}\033[00m" .format(mes)

def help():
#    [Update help]
   print("[Update]")       
   print("$ [].py [options...] logs.txt")       
   print("Available options:")           
   print("[Update]")
   sys.exit(0)

def argsCheck():
    if len(sys.argv) == 1:
        help()

def getFileName() -> str:
    # Get the first non option argument which should be file name
    file_name = ""
    for arg in sys.argv[1:]:
        if arg and not arg.startswith("-"):
            file_name = arg
            break

    if file_name == "":
        print("No file name provided")    
        help()
    else:
        print(Green("File name:"), file_name)
    
    return file_name

def readFile(file_name) -> str: 
    content = ""
    with open(file_name, 'r') as content_file:
        try:
            content = content_file.read()
        except UnicodeDecodeError:
            print(Red("File contains bad symbols. Please, save as plain text. Abort."))    
            sys.exit(0)
    return content    

def getTimestamp(line) -> str:
    match_timestamp = re.search(pattern_timestamp, line)
    if match_timestamp:
        return match_timestamp.group(1)
    else:
        return ""

def getStringByPattern(line, pattern) -> str:
    result = re.search(pattern, line)
    if result:
        if len(result.groups()) != 0:
            return result.group(1)
        else:
            return result.string
    else:
        return ""

def printList(list):
    for item in list:
        print(item)

def hasOption(option) -> bool:
    # Iterate over a list of arguments starting from first element
    for index, arg in enumerate(sys.argv[1:]):    
        # Search ofr arg
        if option == arg:
            return True
    return False

def noOptions() -> bool:
    # Iterate over a list of arguments starting from first element
    for index, arg in enumerate(sys.argv[1:]):    
        # Search for options 
        if arg.startswith("-"):
            return False
    return True

# ##############################################################################################################
# Html generator
# ##############################################################################################################

def createFile(file_name, content):
    
    file = open(file_name, "w")
    file.write(content)
    file.close()

    print("File created: ", file.name)

def openFileInBrowser(file_name):
    # Open file
    if platform.system() == 'Darwin': 
        file_path = os.getcwd() + "/" + file_name
        subprocess.call(('open', file_path))

class HtmlBuilder:

    _html_start_def = """
        <!DOCTYPE html>
        
        <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        </head>
        
        <!-- Styles -->

        <html><body>
            <style>

                div.textbox {
                    margin-right: 70px;
                    margin-left: 70px;
                    width: max-width;
                    height: fit-content;
                    background: #E8E8E8;
                    border-radius: 25px;
                }

                div.text {
                    font-size: 15px;
                    padding: 25px; 
                    font-family: sans-serif;
                    line-height: 1.6;
                    overflow: auto; /* Adds scroll bars */
                    white-space: nowrap; /* Suppresses line breaks */
                }

                div.title {
                    margin-top: 20px;
                    margin-right: 70px;
                    margin-left: 70px;
                    margin-bottom: 20px;
                    font-size: 20px;
                    font-family: sans-serif;
                }

                div.titlecontent {
                    padding-left: 70px;
                    padding-top: 10px;
                }

                span.code {
                    margin-top: 0em;
                    margin-bottom: 1em;
                }

                span.code pre {
                    font-size: 13pt;
                    padding-top: 10px;
                    padding-bottom: 10px;
                    padding-left: 10px;
                    padding-right: 10px;
                    background-color: #f7f7f7;
                    border: solid 1px #d0d0d0;
                    width: max-width;
                    border-radius: 10px;
                    margin-left: 70px;
                    margin-right: 70px;
                    overflow: auto; /* Adds scroll bars */
                    white-space: nowrap; /* Suppresses line breaks */
                }

                #summary {
                    margin-top: 30px;
                    margin-bottom: 40px;
                }

                #summary table {
                    border-collapse: collapse;
                }

                #summary td {
                    vertical-align: top;
                }

                .infoBox {
                    width: 110px;
                    padding-top: 15px;
                    padding-bottom: 15px;
                    text-align: center;
                }

                .infoBox p {
                    margin: 0;
                }

                .summaryGroup {
                    border: solid 2px #d0d0d0;
                    -moz-border-radius: 10px;
                    border-radius: 10px;
                    behavior: url(css3-pie-1.0beta3.htc);
                    margin-left: 70px;
                    margin-right: 70px;
                    width: max-width;
                    font-size: 16pt;
                    padding: 10px;
                }

                div.paragraph {
                    margin-top: 10px;
                    margin-right: 70px;
                    margin-left: 70px;
                    margin-bottom: 20px;
                    font-size: 20px;
                    font-family: sans-serif;
                    width: max-width;
                    border: solid 2px #d0d0d0;
                    -moz-border-radius: 10px;
                    border-radius: 10px;
                    padding-left: 10px;
                }

            </style>
            
            """
    
    _html_end_def = """</body></html>"""

    # Text section piece

    _html_section_def = """
        <div class="title">{}</div>
        <span class="code"><pre>{}</pre></span>
        """
    
    _html_newline_def = "<br/>"

    # Content

    content = ""
    
    # def __init__(self,f_name):
        # self._file_name_info = f_name

    @staticmethod
    def setRedColor(text) -> str:
        _html_red_def = "<span style=\"color: #ff0000\">{}</span>"
        return _html_red_def.format(text)            

    @staticmethod
    def setGreenColor(text) -> str:
        _html_green_def = "<span style=\"color: #088F8F\">{}</span>"
        return _html_green_def.format(text)
    
    @staticmethod
    def setBold(text) -> str:
        _html_bold_def = """<span style="font-weight:bold">{}</span>"""
        return _html_bold_def.format(text)
    
    def addParagraph(self, text):
        _html_paragraph_def = """
            <div class="paragraph" id="">
            <p>{}</p>
            </div>
        """
        formatted = _html_paragraph_def.format(text)
        self.content += formatted

    def addTitle(self, title):
        _html_title_def = """
            <div class="titlecontent">
                <h1>{}</h1>
                </div>
        """
        formatted = _html_title_def.format(title)
        self.content += formatted

    def initPage(self):
        self.content += self._html_start_def

    def addPageSection(self, title, item_list):

        if len(item_list) == 0:
            return           

        text_content = ""
        for item in item_list:
            text_content += item + self._html_newline_def    

        new_html_section = self._html_section_def
        new_section = new_html_section.format(title, text_content)

        self.content += new_section

    def addTable(self, title, subTitle):

        _html_table = """
            <!-- Table with info -->
            
            <table>
            <tr>
            <td>
            <div class="summaryGroup">
            <table>
            <tr>
            <td>
            <div class="infoBox" id="">
            <p>{}</p>
            <p>{}</p>
            </div>
            </td>

            <!-- Uncomment to add more colums

            <td>
            <div class="infoBox" id="failures">
            <div class="counter">1</div>
            <p>failures</p>
            </div>
            </td>
            <td>
            <div class="infoBox" id="skipped">
            <div class="counter">0</div>
            <p>skipped</p>
            </div>
            </td>
            <td>
            <div class="infoBox" id="duration">
            <div class="counter">0.027s</div>
            <p>duration</p>
            </div>
            </td>
            </tr>
            </table>
            </div>
            </td>

            -->

            </tr>
            </table>
            </div>
        """

        formatted = _html_table.format(title, subTitle)

        self.content += formatted

    def build(self) -> str:
        self.content += self._html_end_def
        return self.content

# ######################################################################################################################### 
# Start
# #########################################################################################################################

argsCheck()

file_name = getFileName()
content = readFile(file_name)

file_content = content.splitlines()
lines_number = len(file_content)

print(Green("Number of lines:"), lines_number)

if lines_number == 0:
    print(Red("Empty file, abort."))
    sys.exit()

# Data lists 

# [Update]
match_list = []

# ####################################################################################################################################
# Start log analysis
# ####################################################################################################################################

print(Green("\nStarting..."))

for line in file_content:

    # Do analysis ...

    print("")

# ####################################################################################################################################
# Build Html page
# ####################################################################################################################################

results_file_name = "index.html"

# builder = HtmlBuilder()

# Example
# builder.initPage()
# builder.addTitle("Results")
# builder.addParagraph(HtmlBuilder.setBold("File name: ") + file_name)
# builder.addPageSection("Device info logs", [Update])
# builder.addPageSection("Start logs", match_start_list)
# builder.addPageSection("UI logs", match_ui_screen_list)
# builder.addPageSection("Internal activity logs", [Update])
# builder.addPageSection("Strict Mode Violations logs", match_strict_mode_list)
# builder.addPageSection("Error logs", match_errors_list)

# html_content = builder.build()

# createFile(results_file_name, html_content)
# openFileInBrowser(results_file_name)

print(Green("\n⚽️ DONE ⚽️"))
