import { ArrowDown, ArrowUp } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface MetricCardProps {
  title: string;
  value: string;
  change: string;
  isPositive?: boolean;
}

export const MetricCard = ({ title, value, change, isPositive = true }: MetricCardProps) => {
  return (
    <Card className="bg-card border-border">
      <CardHeader className="pb-2">
        <CardTitle className="text-sm font-medium text-muted-foreground">{title}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col gap-1">
          <div className="text-3xl font-bold text-foreground">{value}</div>
          <div className={`flex items-center gap-1 text-sm ${isPositive ? "text-success" : "text-destructive"}`}>
            {isPositive ? <ArrowUp className="h-4 w-4" /> : <ArrowDown className="h-4 w-4" />}
            <span>{change}</span>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};
