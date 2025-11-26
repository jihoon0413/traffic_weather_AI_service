import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend } from "recharts";

const data = [
  { day: "Mon", actual: 0.65, predicted: 0.68 },
  { day: "Tue", actual: 0.82, predicted: 0.80 },
  { day: "Wed", actual: 0.55, predicted: 0.58 },
  { day: "Thu", actual: 0.78, predicted: 0.75 },
  { day: "Fri", actual: 0.45, predicted: 0.48 },
  { day: "Sat", actual: 0.60, predicted: 0.62 },
  { day: "Sun", actual: 0.90, predicted: 0.88 },
];

export const PredictionChart = () => {
  return (
    <ResponsiveContainer width="100%" height={400}>
      <LineChart data={data} margin={{ top: 20, right: 30, left: 0, bottom: 20 }}>
        <CartesianGrid strokeDasharray="3 3" stroke="hsl(var(--chart-grid))" opacity={0.3} />
        <XAxis 
          dataKey="day" 
          stroke="hsl(var(--muted-foreground))"
          tick={{ fill: "hsl(var(--muted-foreground))" }}
        />
        <YAxis 
          stroke="hsl(var(--muted-foreground))"
          tick={{ fill: "hsl(var(--muted-foreground))" }}
          domain={[0, 1]}
        />
        <Tooltip
          contentStyle={{
            backgroundColor: "hsl(var(--card))",
            border: "1px solid hsl(var(--border))",
            borderRadius: "8px",
          }}
          labelStyle={{ color: "hsl(var(--foreground))" }}
        />
        <Legend 
          wrapperStyle={{ 
            paddingTop: "20px",
          }}
          iconType="line"
        />
        <Line
          type="monotone"
          dataKey="actual"
          stroke="hsl(var(--chart-2))"
          strokeWidth={2}
          dot={{ fill: "hsl(var(--chart-2))", r: 4 }}
          name="Actual"
        />
        <Line
          type="monotone"
          dataKey="predicted"
          stroke="hsl(var(--chart-1))"
          strokeWidth={2}
          strokeDasharray="5 5"
          dot={{ fill: "hsl(var(--chart-1))", r: 4 }}
          name="Predicted"
        />
      </LineChart>
    </ResponsiveContainer>
  );
};
