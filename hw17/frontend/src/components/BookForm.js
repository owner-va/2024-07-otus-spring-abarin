import React, { useEffect, useState } from 'react';
import axios from 'axios';

const BookForm = ({ bookId, onClose, fetchBooks }) => {
  const [title, setTitle] = useState('');
  const [authorId, setAuthorId] = useState('');
  const [genresIds, setGenresIds] = useState([]);
  const [authors, setAuthors] = useState([]);
  const [genres, setGenres] = useState([]);

  useEffect(() => {
    const fetchAuthors = async () => {
      try {
        const response = await axios.get('/api/authors/');
        setAuthors(response.data);
      } catch (error) {
        console.error("The error while retrieving authors:", error);
      }
    };

    const fetchGenres = async () => {
      try {
        const response = await axios.get('/api/genres/');
        setGenres(response.data);
      } catch (error) {
        console.error("The error while retrieving genres:", error);
      }
    };

    fetchAuthors();
    fetchGenres();

    if (bookId) {
      const fetchBookDetails = async () => {
        try {
          const response = await axios.get(`/api/books/${bookId}`);
          const book = response.data;
          setTitle(book.title);
          setAuthorId(book.author.id);
          setGenresIds(book.genres.map(genre => genre.id));
        } catch (error) {
          console.error("The error while retrieving books:", error);
        }
      };
      fetchBookDetails();
    }
  }, [bookId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const bookData = { title, authorId, genresIds };

    try {
      if (bookId) {
        await axios.put(`/api/books/${bookId}`, bookData);
      } else {
        await axios.post('/api/books/', bookData);
      }
      fetchBooks();
      onClose();
    } catch (error) {
      console.error("Error while saving the book:", error);
    }
  };

  return (
    <div className="modal">
      <h2>{bookId ? 'Edit Book' : 'Add Book'}</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Title"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />
        <div>
          <label>Select Author:</label>
          <select value={authorId} onChange={(e) => setAuthorId(e.target.value)} required>
            <option value="">--Select Author--</option>
            {authors.map(author => (
              <option key={author.id} value={author.id}>
                {author.fullName}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label>Select Genres:</label>
          {genres.map(genre => (
            <div key={genre.id}>
              <input
                type="checkbox"
                value={genre.id}
                checked={genresIds.includes(genre.id)}
                onChange={(e) => {
                  const selectedId = genre.id;
                  setGenresIds(prevIds =>
                    e.target.checked
                      ? [...prevIds, selectedId]
                      : prevIds.filter(id => id !== selectedId)
                  );
                }}
              />
              <label>{genre.name}</label>
            </div>
          ))}
        </div>
        <button type="submit">{bookId ? 'Update' : 'Add'}</button>
        <button type="button" onClick={onClose}>Close</button>
      </form>
    </div>
  );
};

export default BookForm;
