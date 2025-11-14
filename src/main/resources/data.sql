-- H2 Database initialization script for development
-- This script will run when the application starts with dev profile

-- Insert some sample firms
INSERT INTO firms_info (id, firm_name, created_by, created_date, updated_date) VALUES
('firm-001', 'Örnek Firma 1', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('firm-002', 'Örnek Firma 2', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('firm-003', 'Test Firması', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert some sample user types
INSERT INTO user_type (id, user_type_name, created_by, created_date, updated_date) VALUES
('user-type-001', 'Admin', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user-type-002', 'User', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('user-type-003', 'Manager', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert some sample inventory categories
INSERT INTO inventory_category (id, category_name, created_by, created_date, updated_date) VALUES
('inv-cat-001', 'Elektronik', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('inv-cat-002', 'Mekanik', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('inv-cat-003', 'Yazılım', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert some sample sites info
INSERT INTO sites_info (id, site_name, site_address, firm_id, created_by, created_date, updated_date) VALUES
('site-001', 'Ana Merkez', 'İstanbul, Türkiye', 'firm-001', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('site-002', 'Şube 1', 'Ankara, Türkiye', 'firm-001', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('site-003', 'Test Sahası', 'İzmir, Türkiye', 'firm-002', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert some sample projects
INSERT INTO projects_info (id, project_name, project_description, firm_id, site_id, created_by, created_date, updated_date) VALUES
('proj-001', 'Proje 1', 'İlk test projesi', 'firm-001', 'site-001', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('proj-002', 'Proje 2', 'İkinci test projesi', 'firm-001', 'site-002', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('proj-003', 'Demo Projesi', 'Demo amaçlı proje', 'firm-002', 'site-003', 'system', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
