# Changelog

## [4.0.0] - 2025-16-02
Author : @RohanMhatre
### Added
- Added simple incremental deployments
- Added flyway support for h2

### Changed
- Moved away from blue-green deployment to simple incremental deployment with minimal downtime.

### Fixed
- Fixed breaking deployment script caused by nginx.conf file not uploaded to container causing it to break.


## [3.0.0] - 2025-14-02 
Author : @RohanMhatre
### Added
- Integrated Flyway for database migrations.
- Ensured schema consistency across deployments.

### Changed
- Removed `ddl-auto: update` to prevent unintended schema modifications.

### Fixed
- Fixed potential schema drift issues due to Hibernate auto-update.

---

## [2.0.0] -  2025-07-02
Author : @RohanMhatre
### Added
- Implemented functionality based on feature requests from Elegen for the take-home test.
- Developed business logic and API endpoints as per requirements.

### Changed
- Improved API response handling and validation.

---

## [1.0.0] -  2025-05-02
Author : @RohanMhatre
### Added
- Created the base structure of the Spring Boot API.
- Set up basic project dependencies, controllers, and services.
- Configured PostgreSQL as the database with JPA.