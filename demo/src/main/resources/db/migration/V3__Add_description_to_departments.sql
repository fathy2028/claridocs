-- Add description column to departments table
ALTER TABLE departments ADD COLUMN description VARCHAR(500);

-- Update existing departments with default descriptions
UPDATE departments SET description = 'Department description' WHERE description IS NULL;
