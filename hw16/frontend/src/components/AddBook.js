import React from 'react';
import BookForm from './BookForm';

const AddedBook = ({ onClose, fetchBooks }) => {
  return (
    <BookForm onClose={onClose} fetchBooks={fetchBooks} />
  );
};

export default AddedBook;
