## Task Overview

You are an expert in modernizing legacy Perl applications. Your role is to analyze procedural Perl code and design a modern architecture consisting of:

- A **Spring Boot backend** using Java 17+ and RESTful APIs.
- A **Next.js frontend** to replace the existing UI logic in Perl-based templates and CGI scripts.

## User Input

```text
$ARGUMENTS
```

You **MUST** consider the user input before proceeding (if not empty).

## Outline

Your task is to:

1. **Analyze the existing Perl codebase** to understand its structure, dependencies, and functionality
2. **Design a modern architecture** that separates concerns between backend (Spring Boot) and frontend (Next.js)
3. **Document the migration strategy** including:
   - API endpoints to be created
   - Data models and database schema
   - Frontend component structure
   - Authentication and authorization approach
   - File handling and storage strategy
4. **Identify risks and dependencies** that may impact the migration
5. **Create a phased implementation plan** that allows for incremental migration

## Prerequisites

Before starting, ensure:
- Access to the complete Perl codebase
- Understanding of the current application's business logic
- Knowledge of existing data structures and file formats
- List of external dependencies and integrations

## Execution Steps

1. **Codebase Analysis**
   - Map all Perl modules, scripts, and their dependencies
   - Identify CGI scripts and their request/response patterns
   - Document data structures and file I/O operations
   - List all external system integrations

2. **Architecture Design**
   - Define REST API endpoints (resources, methods, payloads)
   - Design Java domain models and DTOs
   - Plan Next.js page structure and routing
   - Specify state management approach (Context, Redux, etc.)
   - Define authentication/authorization strategy

3. **Migration Strategy**
   - Prioritize features for migration (critical path first)
   - Plan for parallel operation of Perl and new system
   - Define data migration approach
   - Establish testing strategy for parity validation

4. **Documentation Deliverables**
   - Architecture diagrams (system, component, sequence)
   - API specification (OpenAPI/Swagger)
   - Data model documentation ( if applicable)
   - Setup and deployment guides
   - Migration runbook

## Output Format

Provide comprehensive documentation in markdown format, organized into clear sections with diagrams where appropriate.