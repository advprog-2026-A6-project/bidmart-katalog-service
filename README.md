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

[ ] Category Entity: Implement the hierarchical category schema (e.g., Adjacency List with parent_id or Materialized Path).

[ ] Listing Entity: Create the schema to hold all required fields: title, description, image URL, category reference, starting price, reserve price, and auction duration/end time.

[ ] State Management: Implement an Enum for listing status (e.g., DRAFT, ACTIVE, CANCELLED) to safely track the lifecycle before external auction logic takes over.

## Core CRUD Services & Repositories

[ ] Category Repository/Service: Endpoints/methods to create categories, fetch a category tree, and assign child categories to parents.

[ ] Listing Creation: Implement the POST endpoint for sellers to create a new listing with all the required attributes.

[ ] Listing Retrieval (Basic): Implement a simple GET endpoint to fetch full details of a single listing by its ID, including the seller's placeholder ID, description, and images.

# Milestone 50%: Logika Bisnis Utama & Fungsionalitas Lanjutan
This phase shifts from simple data entry to actual business rules, search strategies, and state transitions.

## Dynamic Search & Filtering

[ ] Specification/Criteria Builder: Implement dynamic queries to allow buyers to search using any combination of: keyword, category, price range, and auction end time.

[ ] Search Endpoint: Create the GET endpoint that accepts these dynamic parameters (e.g., query strings) and returns paginated results.

## Business Logic: Updates & Cancellations

[ ] Update Logic: Implement a method allowing sellers to update only the description and image of a listing.

[ ] Update Constraint: Add a temporary internal check to ensure updates only happen when the listing is in a DRAFT or ACTIVE state (later, you will connect this to the Auction module to verify 0 bids).

[ ] Cancellation Logic: Implement the feature for sellers to cancel a listing. Update the database status to CANCELLED.

[ ] Cancellation Constraint: Similar to the update logic, enforce the rule that cancellation is only allowed if no bids exist (mock this validation for now).

# What to Share for Code Review
To give you the most accurate feedback on whether your 25% implementation is heading in the right direction, I need to see how your foundational layers interact. Please share the following Java/Spring Boot files:

The Entities (Category.java, Listing.java): I want to check your JPA annotations, relationships (@ManyToOne, @OneToMany), and how you mapped the hierarchical structure.

The Repositories (CategoryRepository.java, ListingRepository.java): To see if you are using standard Spring Data JPA methods or if you have started writing custom JPQL queries.

The Service Layer (ListingService.java): This is the most critical part. I want to see how you are handling the business logic for creating a listing and assigning it to a category.

DTOs (Data Transfer Objects): If you are using request/response payloads (e.g., CreateListingRequest.java), share those so I can see how you are handling incoming data before it hits the entity.