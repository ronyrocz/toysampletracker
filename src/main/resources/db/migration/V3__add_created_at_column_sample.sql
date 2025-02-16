-- Add the created_at column with a default value of CURRENT_TIMESTAMP
ALTER TABLE samples
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;

-- Update existing rows to set created_at to the current timestamp
UPDATE samples
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;