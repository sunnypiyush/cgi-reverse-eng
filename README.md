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
Step2 : Using Speckit and Kilo code/copilot try to create a frontend only application 

Step3 : Using Speckit and Kilo code/copilot create a frontend and backend application which has a local json store
         ( note you can also use locally hosted database optionally)

Step4 : Using A11y mcp integration, identify accessibility issues and address them via AI powered solutioning. 


### Prerequisites

- VS code https://code.visualstudio.com/ 
- Kilo code extension setup on VS code - https://marketplace.visualstudio.com/items?itemName=kilocode.Kilo-Code
- Spec Kit installed - https://github.com/github/spec-kit select Kilo code as the integration option.
- For Mac Users install : cpanm 
- For Windows users install Strawberry Perl - https://strawberryperl.com/ 
- AI API access (OpenAI, Anthropic, etc.)

### Installation

```bash
git clone https://github.com/yourusername/cgi-reverse-eng.git
cd cgi-reverse-eng

# Install dependencies for MacOS
curl -L https://cpanmin.us | perl - App::cpanminus
# for Windows with Strawberry Perl cpanm is already included with Strawberry Perl
cpanm plack

run plackup app.cgi
```

### Usage

```bash
python analyze.py --input ./cgi-bin/sample.cgi
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

No License
