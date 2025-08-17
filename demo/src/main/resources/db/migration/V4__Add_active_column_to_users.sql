-- Add active column to users table with default value
ALTER TABLE users ADD COLUMN active BOOLEAN DEFAULT true;

-- Update existing users to have active = true
UPDATE users SET active = true WHERE active IS NULL;

-- Make the column NOT NULL after setting default values
ALTER TABLE users ALTER COLUMN active SET NOT NULL;
