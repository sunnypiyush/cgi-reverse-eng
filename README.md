# cgi-reverse-eng
Sample project to showcase reverse engineering using AI


## Overview

This project demonstrates how AI can assist in reverse engineering legacy CGI applications. It includes sample CGI scripts, analysis tools, and documentation of the reverse engineering process.

## Features

- Legacy CGI script examples
- AI-powered code analysis
- Documentation generation
- Architecture creation 

## Getting Started
Step1 :  Run the project on your local to see the legacy perl project UI. 

Step2 : Using Speckit and Kilo code/copilot try to create a frontend and backend application 

Step3 : Using Speckit and Kilo code/copilot create a frontend and backend application with Accessible UI elements or by providing it UI of your choice
         ( note you can also add ChromaDB or any other locally hosted database for this step)


### Prerequisites

- VS code https://code.visualstudio.com/ 
- Kilo code extension setup on VS code - https://marketplace.visualstudio.com/items?itemName=kilocode.Kilo-Code
- Spec Kit installed - https://github.com/github/spec-kit select Kilo code as the integration option.
- LLM access via API (OpenAI, Anthropic etc.) can be via any cloud provider or direct API with these providers. 

## Prerequisites for running the perl app
- For Mac Users install : cpanm 
- For Windows users install Strawberry Perl - https://strawberryperl.com/ 


### Installation

```bash
git clone https://github.com/piyyadav/cgi-reverse-eng.git
cd cgi-reverse-eng

# Install dependencies for MacOS
curl -L https://cpanmin.us | perl - App::cpanminus
# for Windows with Strawberry Perl cpanm is already included with Strawberry Perl
cpanm plack

run plackup app.cgi
```


## Project Structure

```
cgi-reverse-eng/
├── cgi-bin/          # Sample CGI scripts
├── analysis/         # Analysis results
├── docs/             # Documentation
└── tools/            # Reverse engineering tools
```

## License


