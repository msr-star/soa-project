-- Ensure all 3 restaurants exist with verified owner email and details
INSERT INTO restaurants (id, name, cuisine, address, phone, rating, image_url, is_open, owner_email, created_at)
VALUES 
(1, 'The Royal Biryani & Kebab Palace', 'Hyderabadi, Mughlai, Indian', 'Plot 42, Jubilee Hills, Sector 5', '+91 98765 43210', 4.8, 'https://images.pexels.com/photos/1624487/pexels-photo-1624487.jpeg?auto=compress&cs=tinysrgb&w=800', true, 'owner@cravedash.com', NOW())
ON CONFLICT (id) DO UPDATE SET 
  name = EXCLUDED.name,
  cuisine = EXCLUDED.cuisine,
  rating = EXCLUDED.rating,
  owner_email = 'owner@cravedash.com';

INSERT INTO restaurants (id, name, cuisine, address, phone, rating, image_url, is_open, owner_email, created_at)
VALUES 
(2, 'Artisanal Pasta & Woodfired Crust', 'Italian, Neapolitan, Gourmet', '12 Gourmet Boulevard, High Street', '+91 91234 56789', 4.7, 'https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg?auto=compress&cs=tinysrgb&w=800', true, 'owner@cravedash.com', NOW())
ON CONFLICT (id) DO UPDATE SET 
  name = EXCLUDED.name,
  cuisine = EXCLUDED.cuisine,
  rating = EXCLUDED.rating,
  owner_email = 'owner@cravedash.com';

INSERT INTO restaurants (id, name, cuisine, address, phone, rating, image_url, is_open, owner_email, created_at)
VALUES 
(3, 'Green Harvest Bowls & Grills', 'Healthy, Mediterranean, Fresh', '8 Eco Plaza, Tech Boulevard', '+91 94567 12345', 4.9, 'https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&w=800', true, 'owner@cravedash.com', NOW())
ON CONFLICT (id) DO UPDATE SET 
  name = EXCLUDED.name,
  cuisine = EXCLUDED.cuisine,
  rating = EXCLUDED.rating,
  owner_email = 'owner@cravedash.com';

SELECT setval('restaurants_id_seq', (SELECT MAX(id) FROM restaurants));

-- Clear existing menu items so we can re-map with high accuracy and rich Veg options
DELETE FROM menu_items WHERE restaurant_id IN (1, 2, 3);

-- RESTAURANT 1: The Royal Biryani & Kebab Palace (Hyderabadi, Mughlai, Indian)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_veg, is_available, image_url, created_at)
VALUES
(1, 'Hyderabadi Dum Veg Biryani', 'Aromatic basmati rice layered with garden fresh vegetables, saffron milk, fried onions, and royal spices cooked in sealed handi', 260.00, 'Biryani', true, true, 'https://images.pexels.com/photos/3926124/pexels-photo-3926124.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Shahi Paneer Tikka Biryani', 'Charcoal smoked cottage cheese cubes layered with spiced saffron basmati rice and mint essence', 290.00, 'Biryani', true, true, 'https://images.pexels.com/photos/3926124/pexels-photo-3926124.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Paneer Butter Masala Handi', 'Rich malai paneer cubes simmered in velvety butter tomato gravy infused with kasuri methi', 310.00, 'Main Course', true, true, 'https://images.pexels.com/photos/2474661/pexels-photo-2474661.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Dal Makhani & Garlic Butter Naan', 'Slow-cooked black urad lentils simmered overnight with butter and fresh cream, served with crisp garlic naan', 270.00, 'Main Course', true, true, 'https://images.pexels.com/photos/5410400/pexels-photo-5410400.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Crispy Dahi Ke Kebab', 'Pan-fried golden hung yogurt patties flavored with roasted cumin, green chilies, and served with mint chutney', 240.00, 'Starters', true, true, 'https://images.pexels.com/photos/2233729/pexels-photo-2233729.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Tandoori Malai Chaap', 'Tender soya chaap marinated in fresh clotted cream, cashew cream, and fragrant green cardamom', 250.00, 'Starters', true, true, 'https://images.pexels.com/photos/2233729/pexels-photo-2233729.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Dum Pukht Mutton Biryani', 'Fragrant aged basmati rice cooked on slow flame with tender spiced mutton and Kashmiri saffron', 380.00, 'Biryani', false, true, 'https://images.pexels.com/photos/7394819/pexels-photo-7394819.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Galouti Kebab Melt', 'Mouth-melting Awadhi minced lamb kebabs served with onion rings and mint yogurt relish', 320.00, 'Starters', false, true, 'https://images.pexels.com/photos/2233729/pexels-photo-2233729.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Royal Zafrani Firni', 'Chilled ground rice pudding cooked in thickened milk, infused with royal saffron and slivered pistachios', 140.00, 'Desserts', true, true, 'https://images.pexels.com/photos/3026808/pexels-photo-3026808.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(1, 'Gulab Jamun with Kesar Rabdi', 'Warm khoya dumplings soaked in fragrant cardamom rose syrup, served over thick chilled saffron rabdi', 130.00, 'Desserts', true, true, 'https://images.pexels.com/photos/3026808/pexels-photo-3026808.jpeg?auto=compress&cs=tinysrgb&w=600', NOW());

-- RESTAURANT 2: Artisanal Pasta & Woodfired Crust (Italian, Neapolitan, Gourmet)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_veg, is_available, image_url, created_at)
VALUES
(2, 'Burrata Truffle Margherita Pizza', 'San Marzano tomato sauce, fresh buffalo burrata, sweet basil, and white truffle oil drizzle on fermented sourdough crust', 490.00, 'Pizza', true, true, 'https://images.pexels.com/photos/825661/pexels-photo-825661.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Quattro Formaggi Woodfired Pizza', 'Four-cheese harmony of mozzarella, gorgonzola dolce, aged parmesan, and fontina with thyme honey crust', 460.00, 'Pizza', true, true, 'https://images.pexels.com/photos/825661/pexels-photo-825661.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Garden Bell Pepper & Olive Pizza', 'Fire-roasted bell peppers, Kalamata olives, sundried tomatoes, red onions, and fresh oregano leaves', 420.00, 'Pizza', true, true, 'https://images.pexels.com/photos/825661/pexels-photo-825661.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Wild Mushroom & Truffle Penne', 'Al dente penne tossed in roasted porcini mushroom cream sauce with shaved parmesan and white truffle essence', 410.00, 'Pasta', true, true, 'https://images.pexels.com/photos/1437267/pexels-photo-1437267.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Creamy Spinach & Ricotta Ravioli', 'Handmade artisanal ravioli filled with baby spinach & ricotta in rich hazelnut sage butter emulsion', 430.00, 'Pasta', true, true, 'https://images.pexels.com/photos/1437267/pexels-photo-1437267.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Handmade Tagliatelle Carbonara', 'Silky egg-yolk sauce, crispy smoked pancetta bacon, freshly cracked black pepper, and aged pecorino cheese', 440.00, 'Pasta', false, true, 'https://images.pexels.com/photos/1437267/pexels-photo-1437267.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Crispy Garlic Herb Focaccia', 'Wood-fired Italian focaccia bread brushed with roasted garlic extra-virgin olive oil and sea salt flakes', 180.00, 'Starters', true, true, 'https://images.pexels.com/photos/1437267/pexels-photo-1437267.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Classic Espresso Tiramisu', 'Savoiardi ladyfingers steeped in Italian dark roast espresso layered with airy mascarpone velvet and cocoa', 220.00, 'Desserts', true, true, 'https://images.pexels.com/photos/140831/pexels-photo-140831.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(2, 'Sicilian Lemon Panna Cotta', 'Silky sweet cream custard infused with Madagascar vanilla bean, crowned with tangy Sicilian lemon coulis', 190.00, 'Desserts', true, true, 'https://images.pexels.com/photos/140831/pexels-photo-140831.jpeg?auto=compress&cs=tinysrgb&w=600', NOW());

-- RESTAURANT 3: Green Harvest Bowls & Grills (Healthy, Mediterranean, Fresh)
INSERT INTO menu_items (restaurant_id, name, description, price, category, is_veg, is_available, image_url, created_at)
VALUES
(3, 'Mediterranean Avocado Crunch Bowl', 'Fresh hass avocado, crispy roasted chickpeas, organic quinoa, cucumber ribbons, cherry tomatoes, and lemon tahini dressing', 340.00, 'Salads', true, true, 'https://images.pexels.com/photos/1640770/pexels-photo-1640770.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(3, 'Falafel & Hummus Mezze Platter', 'Golden herb-crusted chickpea falafel, velvety extra virgin olive oil hummus, warm wholewheat pita, and kalamata olives', 320.00, 'Bowls', true, true, 'https://images.pexels.com/photos/1640770/pexels-photo-1640770.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(3, 'Quinoa Greek Feta Salad Bowl', 'Crisp romaine hearts, tri-color quinoa, Persian cucumbers, bell peppers, Kalamata olives, and barrel-aged Greek feta', 290.00, 'Salads', true, true, 'https://images.pexels.com/photos/1640770/pexels-photo-1640770.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(3, 'Roasted Sweet Potato & Black Bean Bowl', 'Chili-lime roasted sweet potatoes, organic black beans, sweet corn, brown rice, guacamole, and cilantro drizzle', 310.00, 'Bowls', true, true, 'https://images.pexels.com/photos/2338407/pexels-photo-2338407.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(3, 'Grilled Citrus Herb Chicken Bowl', 'Flame-grilled tender chicken breast, wild rice pilaf, charred bell peppers, baby spinach, and crumbled feta cheese', 390.00, 'Bowls', false, true, 'https://images.pexels.com/photos/2338407/pexels-photo-2338407.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(3, 'Acai Berry Chia Crunch Bowl', 'Organic Amazonian acai berry puree topped with homemade toasted almond granola, chia seeds, and fresh kiwi slices', 240.00, 'Desserts', true, true, 'https://images.pexels.com/photos/3026808/pexels-photo-3026808.jpeg?auto=compress&cs=tinysrgb&w=600', NOW()),
(3, 'Cold-Pressed Green Detox Elixir', 'Pure 100% cold-pressed English cucumber, crisp green apple, baby spinach leaves, celery, and fresh ginger root', 160.00, 'Desserts', true, true, 'https://images.pexels.com/photos/1640770/pexels-photo-1640770.jpeg?auto=compress&cs=tinysrgb&w=600', NOW());

SELECT setval('menu_items_id_seq', (SELECT MAX(id) FROM menu_items));
