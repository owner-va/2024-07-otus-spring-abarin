import React from 'react';
import BookForm from './BookForm';
const EditBook = ({ bookId, onClose, fetchBooks }) => {
    return (
      <BookForm bookId={bookId} onClose={onClose} fetchBooks={fetchBooks} />
    );
  };
  
  export default EditBook;
  