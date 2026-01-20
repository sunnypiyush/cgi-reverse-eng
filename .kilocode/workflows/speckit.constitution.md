# TaskPad Modernization - Project Constitution

## Product Vision
Transform the legacy Perl-based TaskPad into a modern Java/React application while maintaining simplicity and local JSON storage for rapid prototyping.

## Principles

### Development Philosophy
1. **Prototype-First**: Speed over perfection - get it working, then iterate
2. **Feature Parity**: Match existing Perl functionality before adding new features
3. **Simplicity**: Keep architecture simple, avoid over-engineering
4. **No Testing Overhead**: Skip unit tests, focus on working code
5. **Local-First**: Maintain JSON file storage, no databases

### Technical Constraints
- Java 17+ with Spring Boot (minimal configuration)
- React 18+ with functional components
- Local JSON file storage only
- RESTful API design
- No authentication/authorization
- No deployment configuration

### Quality Bar
- Code compiles and runs without errors
- Manual testing shows feature works
- Basic inline comments for complex logic
- README updated with setup instructions

### Scope Boundaries

#### In Scope
- Task CRUD operations
- JSON file persistence
- Basic UI for task management
- REST API endpoints
- Development setup documentation

#### Out of Scope
- Unit/integration tests
- CI/CD pipelines
- Production deployment
- Database integration
- User authentication
- Advanced error handling
- Performance optimization
- Accessibility features
- Internationalization
- Mobile optimization

## Team Structure
- **Owner**: [Name] - Overall direction
- **Backend Dev**: [Name] - Java/Spring Boot
- **Frontend Dev**: [Name] - React
- **Can be same person for prototype**

## Decision Making
- Quick decisions over consensus
- Document major choices in spec files
- No formal approval process
- Git commits are source of truth

## Success Metrics
1. All Perl features replicated ✓
2. Application runs locally ✓
3. JSON data compatible with Perl version ✓
4. Setup time < 15 minutes ✓

## Evolution Path
When ready for production:
1. Add comprehensive testing
2. Implement proper error handling
3. Set up CI/CD
4. Consider database migration
5. Add authentication
6. Production deployment config

---
**Version**: 1.0  
**Status**: Active  
**Last Updated**: 2026-01-26