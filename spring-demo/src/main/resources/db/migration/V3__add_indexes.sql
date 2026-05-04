-- Indexes in products

CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(price);
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_supplier_id ON products(supplier_id);

-- Indexes in categories

CREATE INDEX IF NOT EXISTS idx_categories_name ON categories(name);

-- Indexes in suppliers

CREATE INDEX IF NOT EXISTS idx_suppliers_email ON suppliers(email);
CREATE INDEX IF NOT EXISTS idx_suppliers_name ON suppliers(name);

-- Indexes in tags

CREATE INDEX IF NOT EXISTS idx_tags_name ON tags(name);