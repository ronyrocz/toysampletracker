-- Create the 'orders' table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY,
    order_uuid UUID NOT NULL
);

-- Create the 'samples' table
CREATE TABLE IF NOT EXISTS samples (
    id BIGINT PRIMARY KEY,
    sample_uuid UUID NOT NULL,
    sequence VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT fk_samples_orders FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Create the 'sample_qc' table
CREATE TABLE IF NOT EXISTS sample_qc (
    id BIGINT PRIMARY KEY,
    plate_id INTEGER NOT NULL,
    qc_1 DOUBLE PRECISION NOT NULL,
    qc_2 DOUBLE PRECISION NOT NULL,
    qc_3 VARCHAR(255) NOT NULL,
    well VARCHAR(255) NOT NULL,
    sample_id BIGINT NOT NULL,
    CONSTRAINT fk_sample_qc_samples FOREIGN KEY (sample_id) REFERENCES samples(id) ON DELETE CASCADE
);
