# AI Research Agent

This project contains a Python AI agent backend and a Kotlin Multiplatform web UI. The agent uses a Gemini/Gemma or DeepSeek language model and Exa for web search and page content retrieval. The UI communicates with the backend over WebSockets.

## Requirements

- Python 3.11 or newer and `pip`
- Java Development Kit (JDK) 17 or newer for the WebUI
- API keys for your selected language model provider and Exa
- Any modern browser

## Setup

### 1. Install Python dependencies

From the project root:

```bash
cd agent
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
python -m pip install -r requirements.txt
cd ..
```

### 2. Configure environment variables

Create `agent/.env` and add your model name, the matching provider API key, your Exa API key, and the WebSocket server address:

```dotenv
MODEL_NAME=gemini-2.5-flash
GEMINI_API_KEY=your_gemini_api_key
# For DeepSeek instead, use a supported DeepSeek model and its key:
# MODEL_NAME=deepseek-chat
# DEEPSEEK_API_KEY=your_deepseek_api_key
EXA_API_KEY=your_exa_api_key
WEBUI_HOST=localhost
WEBUI_PORT=8000
```

Use **either** `GEMINI_API_KEY` or `DEEPSEEK_API_KEY`, matching the provider selected by `MODEL_NAME`. Choose a model name supported by that provider. `EXA_API_KEY` is used by the web-search and page-retrieval tools. Keep this file private and do not commit real API keys.

The WebUI's WebSocket target is configured separately in `web/WebUI/gradle.properties` (`websocket.host` and `websocket.port`). Its defaults are `localhost` and `8000`; if you change `WEBUI_HOST` or `WEBUI_PORT`, update those Gradle properties to match.

## Run

Run commands from the project root. Start the backend and WebUI in **separate terminals** so both remain running:

**Terminal 1 — backend:**

```bash
./run.sh --server
```

**Terminal 2 — WebUI:**

```bash
./run.sh --webui
```

To see the script's available options, run:

```bash
./run.sh --help
```

## Logs

Logs are available in 'agent/log.txt' file.