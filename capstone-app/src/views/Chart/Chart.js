import React from "react";
import {Line} from 'react-chartjs-2';

export default function Chart(props) {
  const data={
    labels: ['May 1st', 'May 2nd', 'May 3rd', 'May 4th', 'May 5th', 'May 6th', 'May 7th'],
    datasets: [{
      label: 'EXCITED',
					backgroundColor: '#A10000',
					borderColor: '#A10000',
					data: [
						0, 3, 5, 4, 5, 2, 1,
					],
					fill: false,
    }, {
      label: 'ALERT',
					backgroundColor: '#0000A1',
					borderColor: '#0000A1',
					data: [
						4, 5, 0, 2, 3, 1, 4,
					],
					fill: false,
          hidden: true,
    }],
  }

  return (
    <div>
      {< Line 
        data={data}
        width={500}
        height={300}
        options={{ maintainAspectRatio: false }}
      />}
    </div>
  );
}