import React, { useEffect, useState } from 'react';
import axios from 'axios';
import AddBook from './AddBook';
import EditBook from './EditBook';

import './styles/styles.css';

const Books = () => {
  const [books, setBooks] = useState([]);
  const [isAddBookVisible, setIsAddBookVisible] = useState(false);
  const [isEditBookVisible, setIsEditBookVisible] = useState(false);
  const [editingBookId, setEditingBookId] = useState(null);

  const fetchBooks = async () => {
    const response = await axios.get('/api/books/');
    setBooks(response.data);
  };

  useEffect(() => {
    fetchBooks();
  }, []);

  const handleDelete = async (bookId) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this book?");
    if (confirmDelete) {
      await axios.delete(`/api/books/${bookId}`);
      fetchBooks();
      setIsAddBookVisible(false);
      setIsEditBookVisible(false);
    }
  };

  const openAddBook = () => {
    setIsAddBookVisible(true);
    setIsEditBookVisible(false);
  };

  const openEditBook = (bookId) => {
    setEditingBookId(bookId);
    setIsEditBookVisible(true);
    setIsAddBookVisible(false);
  };

  return (
    <div>
      <h1>Books</h1>

      <button onClick={openAddBook}>Add Book</button>

      {isAddBookVisible && (
        <AddBook onClose={() => setIsAddBookVisible(false)} fetchBooks={fetchBooks} />
      )}

      {isEditBookVisible && (
        <EditBook
          bookId={editingBookId}
          onClose={() => setIsEditBookVisible(false)}
          fetchBooks={fetchBooks}
        />
      )}

      <table>
        <thead>
          <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Genres</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map((book) => (
            <tr key={book.id}>
              <td>{book.title}</td>
              <td>{book.author.fullName}</td>
              <td>{book.genres.map(genre => genre.name).join(', ')}</td>
              <td>
                <button onClick={() => openEditBook(book.id)}>Edit</button>
                <button onClick={() => handleDelete(book.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Books;
