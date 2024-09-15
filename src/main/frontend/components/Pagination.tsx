import React from 'react';
import CustomButton from './CustomButton';

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({ currentPage, totalPages, onPageChange }) => {
  return (
    <div className="flex justify-center items-center mt-4 space-x-2">
      <CustomButton
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 1}
        className="px-2 py-1 text-sm"
      >
        Previous
      </CustomButton>
      <span className="text-gray-700">
        Page {currentPage} of {totalPages}
      </span>
      <CustomButton
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage === totalPages}
        className="px-2 py-1 text-sm"
      >
        Next
      </CustomButton>
    </div>
  );
};

export default Pagination;