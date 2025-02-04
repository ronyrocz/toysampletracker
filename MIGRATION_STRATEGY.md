# Key Strategies for Safe Data Migrations

- **Backward-Compatible Schema Changes**

  -  Use Additive Changes (Never Remove a Column Directly)
  - Use Nullable Columns First, Then Populate Data
  - Deploy Schema Changes First, Then Deploy Code Changes

---
- **Use Flyway for Versioned Migrations**

    - Modify application-docker.yml to enforce Flyway execution before Hibernate
    - Flyway manages schema changes instead of Hibernate.
    - DDL is disabled (ddl-auto: none) to prevent uncontrolled changes.
---
- **Apply Zero-Downtime Deployment with Safe Migrations**

  - Deploy Schema first
  - Run Fly migration
    - flyway migrate
  - Deploy the New App Version
  - Test if both the old and new versions work fine with the updated schema.
  - Gradually Remove Deprecated Fields
  - After all old versions are phased out, remove unused column
    - ALTER TABLE samples DROP COLUMN old_status; (something like this)
