import React from 'react';
import { Button } from "@vaadin/react-components/Button";

interface CustomButtonProps {
  onClick?: () => void;
  className?: string;
  children: React.ReactNode;
  disabled?: boolean;
}

const CustomButton: React.FC<CustomButtonProps> = ({ onClick, className, children, disabled }) => {
  return (
    <Button
      onClick={onClick}
      className={`bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded ${className}`}
      disabled={disabled}
    >
      {children}
    </Button>
  );
};

export default CustomButton;