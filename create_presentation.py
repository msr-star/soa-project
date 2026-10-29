import sys
from pptx import Presentation
from pptx.util import Inches, Pt
from pptx.dml.color import RGBColor
from pptx.enum.text import PP_ALIGN, MSO_ANCHOR
from pptx.enum.shapes import MSO_SHAPE

def create_deck():
    prs = Presentation()
    # 16:9 Widescreen standard
    prs.slide_width = Inches(13.333)
    prs.slide_height = Inches(7.5)
    blank_layout = prs.slide_layouts[6] # Blank layout

    # Color Palette
    C_NAVY_DARK = RGBColor(15, 23, 42)     # #0F172A
    C_NAVY_SURFACE = RGBColor(30, 41, 59)  # #1E293B
    C_PRIMARY = RGBColor(234, 88, 12)      # #EA580C (Brand Orange)
    C_BLUE = RGBColor(37, 99, 235)         # #2563EB
    C_GREEN = RGBColor(16, 185, 129)       # #10B981
    C_PURPLE = RGBColor(139, 92, 246)      # #8B5CF6
    C_CARD_BG = RGBColor(255, 255, 255)    # #FFFFFF
    C_PAGE_BG = RGBColor(248, 250, 252)    # #F8FAFC
    C_BORDER = RGBColor(226, 232, 240)     # #E2E8F0
    C_TEXT_MAIN = RGBColor(15, 23, 42)     # #0F172A
    C_TEXT_MUTED = RGBColor(100, 116, 139) # #64748B
    C_TEXT_LIGHT = RGBColor(241, 245, 249) # #F1F5F9
    C_WHITE = RGBColor(255, 255, 255)

    def add_header(slide, title, category="24SDCS03R - SOA PROGRAMMING AND MICROSERVICES • REVIEW 1"):
        # Top banner background
        top_bar = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(1.15))
        top_bar.fill.solid()
        top_bar.fill.fore_color.rgb = C_NAVY_DARK
        top_bar.line.color.rgb = C_NAVY_DARK

        # Top Category pill / text
        cat_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.12), Inches(11.7), Inches(0.3))
        tf_cat = cat_box.text_frame
        tf_cat.word_wrap = True
        tf_cat.margin_left = tf_cat.margin_top = tf_cat.margin_right = tf_cat.margin_bottom = 0
        p_cat = tf_cat.paragraphs[0]
        p_cat.text = category.upper()
        p_cat.font.size = Pt(10)
        p_cat.font.bold = True
        p_cat.font.color.rgb = C_PRIMARY

        # Title text
        title_box = slide.shapes.add_textbox(Inches(0.8), Inches(0.42), Inches(9.5), Inches(0.65))
        tf_title = title_box.text_frame
        tf_title.word_wrap = True
        tf_title.margin_left = tf_title.margin_top = tf_title.margin_right = tf_title.margin_bottom = 0
        p_title = tf_title.paragraphs[0]
        p_title.text = title
        p_title.font.size = Pt(22)
        p_title.font.bold = True
        p_title.font.color.rgb = C_WHITE

        # Team HungryDevs badge on top right
        badge = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(10.5), Inches(0.35), Inches(2.1), Inches(0.48))
        badge.fill.solid()
        badge.fill.fore_color.rgb = C_NAVY_SURFACE
        badge.line.color.rgb = C_PRIMARY
        badge.line.width = Pt(1.5)
        tf_b = badge.text_frame
        p_b = tf_b.paragraphs[0]
        p_b.text = "TEAM HUNGRYDEVS"
        p_b.font.size = Pt(10.5)
        p_b.font.bold = True
        p_b.alignment = PP_ALIGN.CENTER
        p_b.font.color.rgb = C_WHITE

        # Page background
        bg = slide.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(1.15), Inches(13.333), Inches(6.35))
        bg.fill.solid()
        bg.fill.fore_color.rgb = C_PAGE_BG
        bg.line.fill.background() # no border

        # Bottom footer bar
        ft_box = slide.shapes.add_textbox(Inches(0.8), Inches(7.15), Inches(11.733), Inches(0.3))
        tf_ft = ft_box.text_frame
        tf_ft.margin_left = tf_ft.margin_top = tf_ft.margin_right = tf_ft.margin_bottom = 0
        p_ft = tf_ft.paragraphs[0]
        p_ft.text = "CraveDash Engine • PS034 • Course 24SDCS03R • KL University CSE • Review 1 Evaluation"
        p_ft.font.size = Pt(9)
        p_ft.font.color.rgb = C_TEXT_MUTED

    def create_card(slide, left, top, width, height, bg_color=C_CARD_BG, border_color=C_BORDER):
        card = slide.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, left, top, width, height)
        card.fill.solid()
        card.fill.fore_color.rgb = bg_color
        card.line.color.rgb = border_color
        card.line.width = Pt(1.2)
        return card

    # =========================================================================
    # SLIDE 1: TITLE & TEAM ROSTER SLIDE
    # =========================================================================
    slide1 = prs.slides.add_slide(blank_layout)
    # Background full dark navy
    bg1 = slide1.shapes.add_shape(MSO_SHAPE.RECTANGLE, Inches(0), Inches(0), Inches(13.333), Inches(7.5))
    bg1.fill.solid()
    bg1.fill.fore_color.rgb = C_NAVY_DARK
    bg1.line.fill.background()

    # Course tag pill
    course_pill = slide1.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Inches(1.0), Inches(0.7), Inches(5.8), Inches(0.42))
    course_pill.fill.solid()
    course_pill.fill.fore_color.rgb = C_NAVY_SURFACE
    course_pill.line.color.rgb = C_PRIMARY
    course_pill.line.width = Pt(1.5)
    p_cp = course_pill.text_frame.paragraphs[0]
    p_cp.text = "24SDCS03R - SOA PROGRAMMING AND MICROSERVICES"
    p_cp.font.size = Pt(11)
    p_cp.font.bold = True
    p_cp.font.color.rgb = C_PRIMARY
    p_cp.alignment = PP_ALIGN.CENTER

    # Project Title
    t_box = slide1.shapes.add_textbox(Inches(1.0), Inches(1.3), Inches(11.3), Inches(1.8))
    tf1 = t_box.text_frame
    tf1.word_wrap = True
    p1 = tf1.paragraphs[0]
    p1.text = "CRAVEDASH MOBILITY SERVICES"
    p1.font.size = Pt(36)
    p1.font.bold = True
    p1.font.color.rgb = C_WHITE

    p2 = tf1.add_paragraph()
    p2.text = "Multi-Restaurant Food Ordering & Delivery Orchestration Engine"
    p2.font.size = Pt(20)
    p2.font.bold = True
    p2.font.color.rgb = RGBColor(56, 189, 248) # Sky blue
    p2.space_before = Pt(8)

    p3 = tf1.add_paragraph()
    p3.text = "Review 1 Project Evaluation • Spring Boot 3.2 • Spring Cloud Eureka • API Gateway • PostgreSQL"
    p3.font.size = Pt(13)
    p3.font.color.rgb = RGBColor(148, 163, 184) # Slate light
    p3.space_before = Pt(6)

    # Team Box Header
    team_hdr = slide1.shapes.add_textbox(Inches(1.0), Inches(3.4), Inches(11.3), Inches(0.4))
    p_th = team_hdr.text_frame.paragraphs[0]
    p_th.text = "PROJECT TEAM: HUNGRYDEVS"
    p_th.font.size = Pt(14)
    p_th.font.bold = True
    p_th.font.color.rgb = C_PRIMARY

    # Team Table
    rows = 4
    cols = 5
    table_shape = slide1.shapes.add_table(rows, cols, Inches(1.0), Inches(3.9), Inches(11.333), Inches(2.5))
    tbl = table_shape.table
    tbl.columns[0].width = Inches(0.9)  # S.No
    tbl.columns[1].width = Inches(3.6)  # Student Name
    tbl.columns[2].width = Inches(1.8)  # Reg No
    tbl.columns[3].width = Inches(2.2)  # Degree & Branch
    tbl.columns[4].width = Inches(2.833) # Project Responsibilities

    headers = ["S.No", "Student Full Name", "Register No", "Degree & Branch", "Core Review 1 Responsibilities"]
    for i, h in enumerate(headers):
        cell = tbl.cell(0, i)
        cell.fill.solid()
        cell.fill.fore_color.rgb = C_NAVY_SURFACE
        p = cell.text_frame.paragraphs[0]
        p.text = h
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = C_WHITE
        p.alignment = PP_ALIGN.CENTER

    team_data = [
        ("1", "GUTHULA SATYA MANIKANTA", "2400031021", "B.Tech. - CSE", "Team Lead • Eureka Discovery • Feign Orchestration"),
        ("2", "MADDURI SAI SRI ROHIT", "2400032311", "B.Tech. - CSE", "Member • Microservices Mesh & PostgreSQL Persistence"),
        ("3", "ALAMURI MURALI KRISHNA DINESH", "2400031588", "B.Tech. - CSE", "Member • API Gateway Routing & JWT RBAC Security")
    ]

    for row_idx, data in enumerate(team_data, start=1):
        for col_idx, val in enumerate(data):
            cell = tbl.cell(row_idx, col_idx)
            cell.fill.solid()
            cell.fill.fore_color.rgb = RGBColor(24, 33, 47) if row_idx % 2 == 1 else RGBColor(19, 27, 39)
            p = cell.text_frame.paragraphs[0]
            p.text = val
            p.font.size = Pt(10.5)
            p.font.color.rgb = C_TEXT_LIGHT
            if col_idx in [0, 2, 3]:
                p.alignment = PP_ALIGN.CENTER

    # Footer note on slide 1
    f_box = slide1.shapes.add_textbox(Inches(1.0), Inches(6.75), Inches(11.3), Inches(0.4))
    pf = f_box.text_frame.paragraphs[0]
    pf.text = "Department of Computer Science & Engineering • Koneru Lakshmaiah Education Foundation (KLEF)"
    pf.font.size = Pt(10)
    pf.font.color.rgb = RGBColor(148, 163, 184)
    pf.alignment = PP_ALIGN.CENTER

    # =========================================================================
    # SLIDE 2: EXECUTIVE SUMMARY & PROBLEM STATEMENT
    # =========================================================================
    slide2 = prs.slides.add_slide(blank_layout)
    add_header(slide2, "Executive Summary & Industry Problem Context")

    # Card 1: The Modern Culinary Commerce Challenge
    c1 = create_card(slide2, Inches(0.8), Inches(1.4), Inches(5.6), Inches(5.4))
    tb1 = slide2.shapes.add_textbox(Inches(1.0), Inches(1.6), Inches(5.2), Inches(5.0))
    tf1 = tb1.text_frame
    tf1.word_wrap = True
    p = tf1.paragraphs[0]
    p.text = "THE PROBLEM DOMAIN"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_PRIMARY

    p = tf1.add_paragraph()
    p.text = "Why Monolithic Food Delivery Fails at Scale"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(12)

    bullets1 = [
        ("Mealtime Concurrency Surges", "Flash lunch and dinner rush traffic causes 10x spikes in menu searches and cart updates. A monolith experiences database connection starvation."),
        ("Cascading Blast Radius", "A crash in payment gateway timeouts locks the entire monolithic servlet container, preventing users from even browsing menus."),
        ("Tight Database Coupling", "Shared single schemas prevent independent table optimizations, creating lock contention between menu edits and order placements."),
        ("Inflexible Deployment", "A single bug fix in restaurant menu descriptions requires redeploying the entire multi-gigabyte monolith with downtime.")
    ]
    for title, desc in bullets1:
        p = tf1.add_paragraph()
        p.text = f"•  {title}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_TEXT_MAIN
        p.space_before = Pt(8)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # Card 2: The CraveDash Microservices Solution
    c2 = create_card(slide2, Inches(6.8), Inches(1.4), Inches(5.7), Inches(5.4))
    tb2 = slide2.shapes.add_textbox(Inches(7.0), Inches(1.6), Inches(5.3), Inches(5.0))
    tf2 = tb2.text_frame
    tf2.word_wrap = True
    p = tf2.paragraphs[0]
    p.text = "THE HUNGRYDEVS ARCHITECTURAL SOLUTION"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_BLUE

    p = tf2.add_paragraph()
    p.text = "Decentralized, High-Throughput Service Mesh"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(12)

    bullets2 = [
        ("Domain-Driven Bounded Contexts", "Autonomous services for Auth, Restaurant Menus, Order Orchestration, and Payment processing running on separate runtimes."),
        ("Perimeter Security & Routing", "Spring Cloud API Gateway acts as single reverse proxy, verifying HMAC-SHA256 JWT tokens before traffic reaches inner microservices."),
        ("Zero Hardcoded Addresses", "Netflix Eureka Service Registry enables dynamic discovery (lb://service-name) and frictionless horizontal instance scaling."),
        ("ACID Relational Persistence", "PostgreSQL (mydb) maintains relational tables for Users, Restaurants, Menu Items, Orders, and Payments with atomic ACID integrity."),
        ("Declarative Inter-Service Feign", "Spring Cloud OpenFeign eliminates boilerplate HTTP clients while preserving loose coupling and clean failure boundaries.")
    ]
    for title, desc in bullets2:
        p = tf2.add_paragraph()
        p.text = f"✓  {title}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_GREEN
        p.space_before = Pt(6)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # =========================================================================
    # SLIDE 3: SYSTEM TOPOLOGY & ARCHITECTURE
    # =========================================================================
    slide3 = prs.slides.add_slide(blank_layout)
    add_header(slide3, "End-to-End System Topology & Architecture Flow")

    # 4 Tier Horizontal Layout
    tiers = [
        ("TIER 1: PRESENTATION", "Client Portal (:3000)", C_BLUE, [
            "Single Page Application (SPA)",
            "Pure stateless JWT token storage",
            "Role-based responsive dashboard (Customer / Owner / Admin)",
            "Live PostgreSQL Database Monitor tab"
        ]),
        ("TIER 2: PERIMETER GATEWAY", "API Gateway (:8080)", C_PRIMARY, [
            "Spring Cloud Gateway reverse proxy",
            "Pre-routing JwtGatewayFilter verification",
            "Identity claims enrichment (X-User-Email, X-User-Role)",
            "Global CORS & dynamic lb:// routing"
        ]),
        ("TIER 3: DISCOVERY & CORE", "Services Mesh (:8081-8084)", C_PURPLE, [
            "Eureka Registry (:8761) heartbeats",
            "Auth Service (:8081) - BCrypt & JWT",
            "Restaurant Service (:8082) - Menus",
            "Order Service (:8083) - OpenFeign",
            "Payment Service (:8084) - Transactions"
        ]),
        ("TIER 4: RELATIONAL STORE", "PostgreSQL (mydb)", C_GREEN, [
            "ACID-compliant relational engine",
            "Tables: users, restaurants, menu_items",
            "Tables: orders, order_items, payments",
            "Strict foreign keys & transactional rollback"
        ])
    ]

    for idx, (tag, name, col, points) in enumerate(tiers):
        left = Inches(0.8 + idx * 2.98)
        c = create_card(slide3, left, Inches(1.4), Inches(2.8), Inches(4.3))
        tb = slide3.shapes.add_textbox(left + Inches(0.15), Inches(1.55), Inches(2.5), Inches(4.0))
        tf = tb.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = tag
        p.font.size = Pt(9.5)
        p.font.bold = True
        p.font.color.rgb = col

        p = tf.add_paragraph()
        p.text = name
        p.font.size = Pt(14)
        p.font.bold = True
        p.font.color.rgb = C_TEXT_MAIN
        p.space_after = Pt(8)

        for pt in points:
            p = tf.add_paragraph()
            p.text = f"• {pt}"
            p.font.size = Pt(10)
            p.font.color.rgb = C_TEXT_MUTED
            p.space_before = Pt(5)

    # API Sequence Flow Box along the bottom
    seq_card = create_card(slide3, Inches(0.8), Inches(5.85), Inches(11.733), Inches(1.15), bg_color=C_NAVY_DARK, border_color=C_NAVY_SURFACE)
    tb_s = slide3.shapes.add_textbox(Inches(1.0), Inches(5.95), Inches(11.3), Inches(0.95))
    tf_s = tb_s.text_frame
    tf_s.word_wrap = True
    p = tf_s.paragraphs[0]
    p.text = "LIVE REQUEST ORCHESTRATION PIPELINE:"
    p.font.size = Pt(10.5)
    p.font.bold = True
    p.font.color.rgb = RGBColor(56, 189, 248)

    p = tf_s.add_paragraph()
    p.text = "1. Client POST /api/orders  ➔  2. Gateway JWT Filter validates token  ➔  3. Eureka resolves lb://order-service  ➔  4. Order calls Restaurant via Feign (Price/Stock check)  ➔  5. Order calls Payment via Feign (Auth charge)  ➔  6. Commit to PostgreSQL mydb"
    p.font.size = Pt(10)
    p.font.color.rgb = C_TEXT_LIGHT
    p.space_before = Pt(4)

    # =========================================================================
    # SLIDE 4: AUTONOMOUS MICROSERVICES BREAKDOWN
    # =========================================================================
    slide4 = prs.slides.add_slide(blank_layout)
    add_header(slide4, "Autonomous Microservices Architecture Breakdown")

    svcs = [
        ("Eureka Service Registry", "Port :8761", "Spring Cloud Netflix Eureka", C_PURPLE,
         "Acts as the service phonebook. Autonomous health checks, dynamic service lookup, and elimination of static host/port dependencies."),
        ("API Gateway Service", "Port :8080", "Spring Cloud Gateway (WebFlux)", C_BLUE,
         "Unified single entrypoint for all clients. Executes JwtGatewayFilter, validates HMAC-SHA256 signatures, CORS negotiation, and lb:// dispatch."),
        ("Authentication Service", "Port :8081", "Spring Boot + Spring Security", C_PRIMARY,
         "Manages user lifecycle and credentials. Uses BCrypt password hashing, issues signed JWT tokens, and maps roles: CUSTOMER, RESTAURANT_OWNER, PLATFORM_ADMIN."),
        ("Restaurant & Menu Service", "Port :8082", "Spring Boot + Spring Data JPA", C_GREEN,
         "Multi-tenant kitchen catalog. Digital menus, dietary classifications (Veg/Non-Veg), live price management, and stock availability toggles."),
        ("Order Orchestration Service", "Port :8083", "Spring Boot + OpenFeign", C_PRIMARY,
         "Coordinates distributed order transactions. Validates order totals, invokes restaurant & payment Feign clients, and maintains state machine."),
        ("Payment Processing Service", "Port :8084", "Spring Boot + Spring Data JPA", C_BLUE,
         "Simulates multi-channel payment settlement (UPI, Cards, NetBanking). Generates cryptographically secure transaction refs and supports rollback.")
    ]

    for idx, (title, port, tech, col, desc) in enumerate(svcs):
        r = idx // 3
        c = idx % 3
        left = Inches(0.8 + c * 3.98)
        top = Inches(1.4 + r * 2.7)
        card = create_card(slide4, left, top, Inches(3.8), Inches(2.5))

        tb = slide4.shapes.add_textbox(left + Inches(0.2), top + Inches(0.15), Inches(3.4), Inches(2.2))
        tf = tb.text_frame
        tf.word_wrap = True

        p = tf.paragraphs[0]
        p.text = f"{port} • {tech}"
        p.font.size = Pt(9.5)
        p.font.bold = True
        p.font.color.rgb = col

        p = tf.add_paragraph()
        p.text = title
        p.font.size = Pt(14)
        p.font.bold = True
        p.font.color.rgb = C_TEXT_MAIN
        p.space_after = Pt(6)

        p = tf.add_paragraph()
        p.text = desc
        p.font.size = Pt(10.5)
        p.font.color.rgb = C_TEXT_MUTED

    # =========================================================================
    # SLIDE 5: SERVICE DISCOVERY & OPENFEIGN COMMUNICATION
    # =========================================================================
    slide5 = prs.slides.add_slide(blank_layout)
    add_header(slide5, "Service Discovery & Declarative Feign Inter-Service Communication")

    # Left Card: Eureka Discovery
    c_e = create_card(slide5, Inches(0.8), Inches(1.4), Inches(5.6), Inches(5.4))
    tb_e = slide5.shapes.add_textbox(Inches(1.0), Inches(1.6), Inches(5.2), Inches(5.0))
    tf_e = tb_e.text_frame
    tf_e.word_wrap = True

    p = tf_e.paragraphs[0]
    p.text = "SERVICE DISCOVERY WITH EUREKA (:8761)"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_PURPLE

    p = tf_e.add_paragraph()
    p.text = "Decentralized Registration & Health Heartbeats"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(10)

    eureka_pts = [
        ("@EnableEurekaServer Registry", "Runs standalone on port 8761, maintaining an in-memory routing table of all healthy microservice instances."),
        ("@EnableDiscoveryClient", "Every service registers itself at boot time with application name (e.g. AUTH-SERVICE, RESTAURANT-SERVICE, ORDER-SERVICE)."),
        ("Heartbeat Lease Renewal", "Services send 30-second periodic heartbeat pings. Eureka automatically de-registers unresponsive nodes to prevent stale routing."),
        ("Load-Balanced Dispatch (lb://)", "The API Gateway routes traffic via lb://RESTAURANT-SERVICE rather than hardcoded URLs, enabling zero-downtime scaling.")
    ]
    for h, d in eureka_pts:
        p = tf_e.add_paragraph()
        p.text = f"• {h}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_TEXT_MAIN
        p.space_before = Pt(8)
        run = p.add_run()
        run.text = d
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # Right Card: OpenFeign Declarative Clients
    c_f = create_card(slide5, Inches(6.8), Inches(1.4), Inches(5.7), Inches(5.4))
    tb_f = slide5.shapes.add_textbox(Inches(7.0), Inches(1.6), Inches(5.3), Inches(5.0))
    tf_f = tb_f.text_frame
    tf_f.word_wrap = True

    p = tf_f.paragraphs[0]
    p.text = "DECLARATIVE REST WITH OPENFEIGN"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_BLUE

    p = tf_f.add_paragraph()
    p.text = "Seamless Synchronous Microservice Orchestration"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(10)

    feign_pts = [
        ("Declarative Interface Contracts", "Spring Cloud OpenFeign allows Order Service to communicate with other services using simple Java interfaces without RestTemplate boilerplate."),
        ("RestaurantClient Integration", "Order Service calls RestaurantClient.getMenuItem(id) to fetch canonical item names, prices, and verify stock availability before order creation."),
        ("PaymentClient Integration", "Order Service calls PaymentClient.processPayment(req) to synchronously authorize charges and capture unique transaction references."),
        ("Internal Header Propagation", "Orders forward security headers (X-Gateway-Secret, X-User-Email) across Feign calls, maintaining defense-in-depth across service boundaries.")
    ]
    for h, d in feign_pts:
        p = tf_f.add_paragraph()
        p.text = f"✓ {h}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_GREEN
        p.space_before = Pt(8)
        run = p.add_run()
        run.text = d
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # =========================================================================
    # SLIDE 6: SECURITY, JWT & DEFENSE-IN-DEPTH RBAC
    # =========================================================================
    slide6 = prs.slides.add_slide(blank_layout)
    add_header(slide6, "Enterprise Security: HMAC-SHA256 JWT & Defense-in-Depth RBAC")

    # 3 Column Cards
    sec_cards = [
        ("1. AUTHENTICATION & HASHING", "BCrypt & JWT Token Issuance", C_PRIMARY, [
            "User credentials submitted to Auth Service (:8081).",
            "Passwords secured with salted BCrypt hashing (no plain text passwords stored in DB).",
            "Issues cryptographically signed HMAC-SHA256 JWT token.",
            "Contains verified claims: sub (email), role, userId, exp."
        ]),
        ("2. GATEWAY PERIMETER FILTER", "JwtGatewayFilter Verification", C_BLUE, [
            "Intercepts all inbound HTTP requests at API Gateway (:8080).",
            "Bypasses public routes (/api/auth/**, health pings).",
            "Extracts Bearer token and validates signature and expiry.",
            "Enriches downstream headers: X-User-Email, X-User-Role, X-Gateway-Secret."
        ]),
        ("3. DEFENSE-IN-DEPTH RBAC", "Strict Role Segregation", C_GREEN, [
            "CUSTOMER: Explore restaurants, view digital menus, add to basket, execute orders.",
            "RESTAURANT_OWNER: Add new menu dishes, delete items, toggle stock, manage kitchen prep.",
            "PLATFORM_ADMIN: Live database monitor, system inspection, multi-tenant audit."
        ])
    ]

    for idx, (tag, name, col, points) in enumerate(sec_cards):
        left = Inches(0.8 + idx * 3.98)
        card = create_card(slide6, left, Inches(1.4), Inches(3.8), Inches(4.2))
        tb = slide6.shapes.add_textbox(left + Inches(0.2), Inches(1.55), Inches(3.4), Inches(3.9))
        tf = tb.text_frame
        tf.word_wrap = True

        p = tf.paragraphs[0]
        p.text = tag
        p.font.size = Pt(10)
        p.font.bold = True
        p.font.color.rgb = col

        p = tf.add_paragraph()
        p.text = name
        p.font.size = Pt(15)
        p.font.bold = True
        p.font.color.rgb = C_TEXT_MAIN
        p.space_after = Pt(8)

        for pt in points:
            p = tf.add_paragraph()
            p.text = f"• {pt}"
            p.font.size = Pt(10.5)
            p.font.color.rgb = C_TEXT_MUTED
            p.space_before = Pt(5)

    # JWT Claim breakdown banner at the bottom
    jwt_card = create_card(slide6, Inches(0.8), Inches(5.8), Inches(11.733), Inches(1.2), bg_color=C_NAVY_DARK, border_color=C_NAVY_SURFACE)
    tb_j = slide6.shapes.add_textbox(Inches(1.0), Inches(5.9), Inches(11.3), Inches(1.0))
    tf_j = tb_j.text_frame
    tf_j.word_wrap = True

    p = tf_j.paragraphs[0]
    p.text = "CRYPTOGRAPHIC TOKEN PAYLOAD (HMAC-SHA256 SIGNED):"
    p.font.size = Pt(10.5)
    p.font.bold = True
    p.font.color.rgb = RGBColor(56, 189, 248)

    p = tf_j.add_paragraph()
    p.text = '{\n  "sub": "customer@cravedash.com",  "role": "ROLE_CUSTOMER",  "userId": 1,  "exp": 1790102400  }\n→ Validated at Gateway perimeter before downstream routing to order & restaurant services.'
    p.font.size = Pt(9.5)
    p.font.color.rgb = C_TEXT_LIGHT
    p.space_before = Pt(3)

    # =========================================================================
    # SLIDE 7: ORDER ORCHESTRATION & STATE MACHINE
    # =========================================================================
    slide7 = prs.slides.add_slide(blank_layout)
    add_header(slide7, "Order Orchestration & Distributed State Machine")

    # State machine flow pills
    states = [
        ("1. PENDING", "Order received; cart validated via Feign", C_BLUE),
        ("2. CONFIRMED", "Payment authorized; transaction ref saved", C_GREEN),
        ("3. PREPARING", "Kitchen accepts ticket & cooks meal", C_PRIMARY),
        ("4. OUT FOR DELIVERY", "Driver assigned & transit initiated", C_PURPLE),
        ("5. DELIVERED", "Order complete at customer address", C_GREEN)
    ]
    for idx, (st_name, st_desc, col) in enumerate(states):
        left = Inches(0.8 + idx * 2.38)
        card = create_card(slide7, left, Inches(1.4), Inches(2.2), Inches(1.6))
        tb = slide7.shapes.add_textbox(left + Inches(0.1), Inches(1.5), Inches(2.0), Inches(1.4))
        tf = tb.text_frame
        tf.word_wrap = True
        p = tf.paragraphs[0]
        p.text = st_name
        p.font.size = Pt(11)
        p.font.bold = True
        p.font.color.rgb = col

        p = tf.add_paragraph()
        p.text = st_desc
        p.font.size = Pt(9.5)
        p.font.color.rgb = C_TEXT_MUTED
        p.space_before = Pt(4)

    # Bottom Two Comparison Cards
    c_flow = create_card(slide7, Inches(0.8), Inches(3.2), Inches(5.7), Inches(3.6))
    tb_fl = slide7.shapes.add_textbox(Inches(1.0), Inches(3.35), Inches(5.3), Inches(3.3))
    tf_fl = tb_fl.text_frame
    tf_fl.word_wrap = True

    p = tf_fl.paragraphs[0]
    p.text = "HAPPY PATH TRANSACTION ORCHESTRATION"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_GREEN

    p = tf_fl.add_paragraph()
    p.text = "Multi-Service Coordination via Feign"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(6)

    hp_steps = [
        ("Step 1: Cart Ingestion", "Client submits restaurantId, items array, deliveryAddress, and paymentMethod."),
        ("Step 2: Feign Price Lock", "Order Service queries Restaurant Service to ensure items are active and prices have not been tampered with on client."),
        ("Step 3: Feign Payment Charge", "Order Service delegates to Payment Service. Returns SUCCESS with unique chargeRef."),
        ("Step 4: Atomic Persist", "Order entity + child OrderItems saved in PostgreSQL with status CONFIRMED.")
    ]
    for title, desc in hp_steps:
        p = tf_fl.add_paragraph()
        p.text = f"• {title}: "
        p.font.bold = True
        p.font.size = Pt(10)
        p.font.color.rgb = C_TEXT_MAIN
        p.space_before = Pt(4)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # Rollback card
    c_fail = create_card(slide7, Inches(6.8), Inches(3.2), Inches(5.7), Inches(3.6))
    tb_fa = slide7.shapes.add_textbox(Inches(7.0), Inches(3.35), Inches(5.3), Inches(3.3))
    tf_fa = tb_fa.text_frame
    tf_fa.word_wrap = True

    p = tf_fa.paragraphs[0]
    p.text = "COMPENSATION & FAILURE HANDLING"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_PRIMARY

    p = tf_fa.add_paragraph()
    p.text = "Resilience Against Partial Outages"
    p.font.size = Pt(15)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(6)

    comp_steps = [
        ("Payment Rejection / Timeout", "If payment method is invalid or bank simulator returns FAIL, order status transitions to CANCELLED."),
        ("Stock Depletion Prevention", "If restaurant item is marked isAvailable=false, order checkout fails immediately before payment is charged."),
        ("No Orphaned Records", "Transactional boundaries ensure that failed payment attempts never leave partial or unpaid pending orders in the system."),
        ("Client Notification", "Clear diagnostic error message returned to user with HTTP 400 Bad Request.")
    ]
    for title, desc in comp_steps:
        p = tf_fa.add_paragraph()
        p.text = f"⚠️ {title}: "
        p.font.bold = True
        p.font.size = Pt(10)
        p.font.color.rgb = C_PRIMARY
        p.space_before = Pt(4)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # =========================================================================
    # SLIDE 8: RELATIONAL DATABASE ARCHITECTURE (POSTGRESQL MYDB)
    # =========================================================================
    slide8 = prs.slides.add_slide(blank_layout)
    add_header(slide8, "Relational Database Architecture: PostgreSQL (mydb)")

    # 4 Database Table Cards
    tables_info = [
        ("public.users", "Identity & RBAC", C_PRIMARY, [
            "id BIGSERIAL PRIMARY KEY",
            "email VARCHAR(255) UNIQUE",
            "password_hash VARCHAR(255) (BCrypt)",
            "full_name VARCHAR(150)",
            "role VARCHAR(50) (CUSTOMER / OWNER / ADMIN)",
            "created_at TIMESTAMP"
        ]),
        ("public.restaurants", "Tenant Kitchens", C_BLUE, [
            "id BIGSERIAL PRIMARY KEY",
            "name VARCHAR(255)",
            "cuisine VARCHAR(255)",
            "address VARCHAR(255)",
            "rating NUMERIC(2,1)",
            "owner_email VARCHAR(255)"
        ]),
        ("public.menu_items", "Digital Catalog", C_GREEN, [
            "id BIGSERIAL PRIMARY KEY",
            "restaurant_id BIGINT (FK -> restaurants)",
            "name VARCHAR(255)",
            "price NUMERIC(10,2)",
            "is_veg BOOLEAN (Veg 🟢 / Non-Veg 🔴)",
            "category VARCHAR(100)",
            "is_available BOOLEAN"
        ]),
        ("public.orders & items", "Transactional Commerce", C_PURPLE, [
            "id BIGSERIAL PRIMARY KEY",
            "order_number VARCHAR(100) UNIQUE",
            "user_email VARCHAR(255)",
            "total_amount NUMERIC(10,2)",
            "status VARCHAR(50)",
            "payment_tx_ref VARCHAR(255)",
            "order_items (order_id FK, quantity, price)"
        ])
    ]

    for idx, (t_name, t_purpose, col, schema) in enumerate(tables_info):
        left = Inches(0.8 + idx * 2.98)
        card = create_card(slide8, left, Inches(1.4), Inches(2.8), Inches(4.3))
        tb = slide8.shapes.add_textbox(left + Inches(0.15), Inches(1.55), Inches(2.5), Inches(4.0))
        tf = tb.text_frame
        tf.word_wrap = True

        p = tf.paragraphs[0]
        p.text = t_purpose.upper()
        p.font.size = Pt(9.5)
        p.font.bold = True
        p.font.color.rgb = col

        p = tf.add_paragraph()
        p.text = t_name
        p.font.size = Pt(14)
        p.font.bold = True
        p.font.color.rgb = C_TEXT_MAIN
        p.space_after = Pt(8)

        for col_def in schema:
            p = tf.add_paragraph()
            p.text = f"• {col_def}"
            p.font.size = Pt(9.5)
            p.font.color.rgb = C_TEXT_MUTED
            p.space_before = Pt(4)

    # ACID Highlight Banner
    acid_card = create_card(slide8, Inches(0.8), Inches(5.85), Inches(11.733), Inches(1.15), bg_color=C_NAVY_DARK, border_color=C_NAVY_SURFACE)
    tb_a = slide8.shapes.add_textbox(Inches(1.0), Inches(5.95), Inches(11.3), Inches(0.95))
    tf_a = tb_a.text_frame
    tf_a.word_wrap = True

    p = tf_a.paragraphs[0]
    p.text = "ACID GUARANTEES IN CRAVEDASH POSTGRESQL ENGINE:"
    p.font.size = Pt(10.5)
    p.font.bold = True
    p.font.color.rgb = RGBColor(56, 189, 248)

    p = tf_a.add_paragraph()
    p.text = "• Atomicity: Order header + order line items commit atomically or roll back completely.\n• Consistency: Foreign keys prevent orphaned menu items and order items.\n• Isolation: READ COMMITTED prevents dirty reads during concurrent cart updates."
    p.font.size = Pt(9.5)
    p.font.color.rgb = C_TEXT_LIGHT
    p.space_before = Pt(2)

    # =========================================================================
    # SLIDE 9: REVIEW 1 LIVE DATABASE INSERTION & DELETION SHOWCASE
    # =========================================================================
    slide9 = prs.slides.add_slide(blank_layout)
    add_header(slide9, "Review 1 Core Live Operations: Database Insertion & Deletion Showcase")

    # Card 1: INSERTION
    c_ins = create_card(slide9, Inches(0.8), Inches(1.4), Inches(5.6), Inches(5.4))
    tb_ins = slide9.shapes.add_textbox(Inches(1.0), Inches(1.6), Inches(5.2), Inches(5.0))
    tf_ins = tb_ins.text_frame
    tf_ins.word_wrap = True

    p = tf_ins.paragraphs[0]
    p.text = "CORE REQUIREMENT 1: RELATIONAL INSERTION"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_GREEN

    p = tf_ins.add_paragraph()
    p.text = "Live Menu Item Addition to PostgreSQL"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(8)

    ins_bullets = [
        ("Action Flow", "Restaurant Partner opens 'Partner Management' portal, selects kitchen, and clicks '+ Add New Dish'."),
        ("Endpoint Triggered", "POST /api/restaurants/{id}/menu with JWT Authorization Bearer token."),
        ("SQL Execution", "INSERT INTO menu_items (restaurant_id, name, price, is_veg, category, is_available, description) VALUES (?, ?, ?, ?, ?, ?, ?);"),
        ("Live Verification", "Item is immediately selectable on Customer digital menu and visible in the live PostgreSQL 'mydb' Database Monitor table.")
    ]
    for h, d in ins_bullets:
        p = tf_ins.add_paragraph()
        p.text = f"✓ {h}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_GREEN
        p.space_before = Pt(8)
        run = p.add_run()
        run.text = d
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # Card 2: DELETION
    c_del = create_card(slide9, Inches(6.8), Inches(1.4), Inches(5.7), Inches(5.4))
    tb_del = slide9.shapes.add_textbox(Inches(7.0), Inches(1.6), Inches(5.3), Inches(5.0))
    tf_del = tb_del.text_frame
    tf_del.word_wrap = True

    p = tf_del.paragraphs[0]
    p.text = "CORE REQUIREMENT 2: RELATIONAL DELETION"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_PRIMARY

    p = tf_del.add_paragraph()
    p.text = "Live Menu Item Deletion from PostgreSQL"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(8)

    del_bullets = [
        ("Action Flow", "Restaurant Partner identifies discontinued or seasonal dish in menu list and clicks 'Delete' button."),
        ("Endpoint Triggered", "DELETE /api/restaurants/{id}/menu/{itemId} protected by RESTAURANT_OWNER role check."),
        ("SQL Execution", "DELETE FROM menu_items WHERE id = ? AND restaurant_id = ?;"),
        ("Immediate Effect", "Record is deleted from PostgreSQL mydb. Customer portal instantly ceases offering the item; DB monitor counter decrements.")
    ]
    for h, d in del_bullets:
        p = tf_del.add_paragraph()
        p.text = f"🗑 {h}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_PRIMARY
        p.space_before = Pt(8)
        run = p.add_run()
        run.text = d
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # =========================================================================
    # SLIDE 10: REVIEW 1 EVALUATION RUBRICS DEFENSE (TARGET 5/5)
    # =========================================================================
    slide10 = prs.slides.add_slide(blank_layout)
    add_header(slide10, "Review 1 Evaluation Rubrics Mapping (Target: 5/5 Score)")

    rubrics = [
        ("1. Problem Analysis & Requirement Specification", "Level 5 / 5 Score • 10 / 10 Weightage", C_BLUE, [
            "Decomposed complex multi-restaurant food delivery domain into isolated contexts.",
            "Complete multi-persona specifications: Customer, Restaurant Partner, Platform Admin.",
            "Formally implemented order state machine from PENDING through DELIVERED with failure rollback."
        ]),
        ("2. Microservice Identification & Service Discovery", "Level 5 / 5 Score • 10 / 10 Weightage", C_PURPLE, [
            "6 fully autonomous microservices running on dedicated HTTP ports (8761, 8080, 8081, 8082, 8083, 8084).",
            "Netflix Eureka dynamic service registry eliminates hardcoded IP addresses.",
            "Spring Cloud OpenFeign handles declarative inter-service HTTP contracts with client-side load balancing."
        ]),
        ("3. JWT Authentication & Role-Based Access Control", "Level 5 / 5 Score • 10 / 10 Weightage", C_PRIMARY, [
            "Stateless token-based security using signed HMAC-SHA256 tokens with encrypted claims.",
            "BCrypt password hashing prevents cleartext storage in PostgreSQL.",
            "Strict RBAC guards: CUSTOMER cannot mutate menus; RESTAURANT_OWNER cannot modify platform users."
        ]),
        ("4. API Gateway Configuration & Dynamic Routing", "Level 5 / 5 Score • 10 / 10 Weightage", C_GREEN, [
            "Spring Cloud Gateway acts as unified reverse proxy on port 8080.",
            "JwtGatewayFilter executes perimeter signature verification and header enrichment.",
            "Dynamic lb:// service routing and global CORS negotiation enable seamless frontend requests."
        ])
    ]

    for idx, (title, score, col, points) in enumerate(rubrics):
        r = idx // 2
        c = idx % 2
        left = Inches(0.8 + c * 5.98)
        top = Inches(1.4 + r * 2.7)
        card = create_card(slide10, left, top, Inches(5.8), Inches(2.5))

        tb = slide10.shapes.add_textbox(left + Inches(0.2), top + Inches(0.12), Inches(5.4), Inches(2.2))
        tf = tb.text_frame
        tf.word_wrap = True

        p = tf.paragraphs[0]
        p.text = score
        p.font.size = Pt(10)
        p.font.bold = True
        p.font.color.rgb = col

        p = tf.add_paragraph()
        p.text = title
        p.font.size = Pt(13.5)
        p.font.bold = True
        p.font.color.rgb = C_TEXT_MAIN
        p.space_after = Pt(4)

        for pt in points:
            p = tf.add_paragraph()
            p.text = f"✓ {pt}"
            p.font.size = Pt(10)
            p.font.color.rgb = C_TEXT_MUTED
            p.space_before = Pt(3)

    # =========================================================================
    # SLIDE 11: CONCLUSION & REVIEW 2 ROADMAP
    # =========================================================================
    slide11 = prs.slides.add_slide(blank_layout)
    add_header(slide11, "Review 1 Summary & Review 2 Engineering Roadmap")

    # Left Card: Review 1 Completed Milestones
    c_r1 = create_card(slide11, Inches(0.8), Inches(1.4), Inches(5.6), Inches(5.4))
    tb_r1 = slide11.shapes.add_textbox(Inches(1.0), Inches(1.6), Inches(5.2), Inches(5.0))
    tf_r1 = tb_r1.text_frame
    tf_r1.word_wrap = True

    p = tf_r1.paragraphs[0]
    p.text = "REVIEW 1 ACCOMPLISHMENTS"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_GREEN

    p = tf_r1.add_paragraph()
    p.text = "Engine Highlights & Live Operations"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(10)

    r1_pts = [
        ("6 Autonomous Services Deployed", "All running healthy on ports 8761, 8080, 8081, 8082, 8083, and 8084."),
        ("Production PostgreSQL Persistence", "Live database (mydb) with 3 restaurants, 26 dishes (22 Veg 🟢), and active user accounts."),
        ("Verified Database Operations", "Flawless demonstration of live relational INSERTION (add dish) and DELETION (remove dish)."),
        ("Perimeter JWT Security", "HMAC-SHA256 signature verification in Gateway with downstream header injection."),
        ("Full Stack Presentation", "Responsive web interface with live PostgreSQL Database Monitor and architecture flow visualization.")
    ]
    for title, desc in r1_pts:
        p = tf_r1.add_paragraph()
        p.text = f"✓ {title}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_GREEN
        p.space_before = Pt(6)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # Right Card: Review 2 Engineering Roadmap
    c_r2 = create_card(slide11, Inches(6.8), Inches(1.4), Inches(5.7), Inches(5.4))
    tb_r2 = slide11.shapes.add_textbox(Inches(7.0), Inches(1.6), Inches(5.3), Inches(5.0))
    tf_r2 = tb_r2.text_frame
    tf_r2.word_wrap = True

    p = tf_r2.paragraphs[0]
    p.text = "REVIEW 2 ENGINEERING ROADMAP"
    p.font.size = Pt(11)
    p.font.bold = True
    p.font.color.rgb = C_BLUE

    p = tf_r2.add_paragraph()
    p.text = "Advanced Distributed Patterns Planned"
    p.font.size = Pt(17)
    p.font.bold = True
    p.font.color.rgb = C_TEXT_MAIN
    p.space_after = Pt(10)

    r2_pts = [
        ("Event-Driven Architecture (Apache Kafka)", "Publish-subscribe event topics for order status updates, kitchen notifications, and driver dispatches."),
        ("Distributed Cache (Redis)", "In-memory caching of high-velocity menu reads to achieve sub-millisecond latency during flash lunch sales."),
        ("Resilience4j Circuit Breakers", "Fault-tolerant circuit breakers, fallback degradation methods, and rate-limiting at the API Gateway."),
        ("Real-Time Driver Tracking (WebSockets)", "Live geo-location updates of delivery fleets streamed directly to the customer map interface.")
    ]
    for title, desc in r2_pts:
        p = tf_r2.add_paragraph()
        p.text = f"🚀 {title}: "
        p.font.bold = True
        p.font.size = Pt(11)
        p.font.color.rgb = C_BLUE
        p.space_before = Pt(8)
        run = p.add_run()
        run.text = desc
        run.font.bold = False
        run.font.color.rgb = C_TEXT_MUTED

    # Save presentation
    output_path = "C:\\Users\\msroh\\.gemini\\antigravity\\scratch\\cravedash-engine\\CraveDash_Review_1_Presentation.pptx"
    prs.save(output_path)
    print(f"SUCCESS: Presentation saved to {output_path}")

if __name__ == "__main__":
    create_deck()
