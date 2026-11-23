package com.saad.tasks;

class ConflictException extends RuntimeException { ConflictException() { super("Record already exists."); } }
