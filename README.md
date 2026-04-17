---
title: BidMart Catalog Service
emoji: 🛒
colorFrom: blue
colorTo: green
sdk: docker
pinned: false
---

# Milestone 25%: Skema Database & Fitur Inti (CRUD Dasar)
This phase is all about structural foundation. You need to ensure data is stored efficiently and basic data entry works.

## Database Schema & Entities

[x] Category Entity: Implement the hierarchical category schema (e.g., Adjacency List with parent_id or Materialized Path).

[x] Listing Entity: Create the schema to hold all required fields: title, description, image URL, category reference, starting price, reserve price, and auction duration/end time.

[x] State Management: Implement an Enum for listing status (e.g., DRAFT, ACTIVE, CANCELLED) to safely track the lifecycle before external auction logic takes over.

## Core CRUD Services & Repositories

[x] Category Repository/Service: Endpoints/methods to create categories, fetch a category tree, and assign child categories to parents.

[x] Listing Creation: Implement the POST endpoint for sellers to create a new listing with all the required attributes.

[x] Listing Retrieval (Basic): Implement a simple GET endpoint to fetch full details of a single listing by its ID, including the seller's placeholder ID, description, and images.

# Milestone 50%: Logika Bisnis Utama & Fungsionalitas Lanjutan
This phase shifts from simple data entry to actual business rules, search strategies, and state transitions.

## Dynamic Search & Filtering

[x] Specification/Criteria Builder: Implement dynamic queries to allow buyers to search using any combination of: keyword, category, price range, and auction end time.

[x] Search Endpoint: Create the GET endpoint that accepts these dynamic parameters (e.g., query strings) and returns paginated results.



