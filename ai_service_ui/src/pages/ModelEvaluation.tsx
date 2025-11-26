import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { MetricCard } from "@/components/MetricCard";
import { PredictionChart } from "@/components/PredictionChart";
import { ClassificationTable } from "@/components/ClassificationTable";
import { Download } from "lucide-react";

const ModelEvaluation = () => {
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold text-foreground mb-2">LSTM Model Evaluation</h1>
          <p className="text-muted-foreground">Performance analysis for congestion prediction model.</p>
        </div>
        <Button className="bg-primary text-primary-foreground hover:bg-primary/90">
          <Download className="h-4 w-4 mr-2" />
          Export Data
        </Button>
      </div>

      {/* Filters */}
      <div className="flex gap-4">
        <Select defaultValue="city-a">
          <SelectTrigger className="w-[200px] bg-secondary border-border">
            <SelectValue placeholder="Dataset" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="city-a">Dataset: City Traffic A</SelectItem>
            <SelectItem value="city-b">Dataset: City Traffic B</SelectItem>
            <SelectItem value="city-c">Dataset: City Traffic C</SelectItem>
          </SelectContent>
        </Select>

        <Select defaultValue="7days">
          <SelectTrigger className="w-[200px] bg-secondary border-border">
            <SelectValue placeholder="Time Range" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="7days">Time Range: Last 7 Days</SelectItem>
            <SelectItem value="14days">Time Range: Last 14 Days</SelectItem>
            <SelectItem value="30days">Time Range: Last 30 Days</SelectItem>
          </SelectContent>
        </Select>

        <Select defaultValue="v2.1.3">
          <SelectTrigger className="w-[200px] bg-secondary border-border">
            <SelectValue placeholder="Model Version" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="v2.1.3">Model Version: v2.1.3</SelectItem>
            <SelectItem value="v2.1.2">Model Version: v2.1.2</SelectItem>
            <SelectItem value="v2.1.1">Model Version: v2.1.1</SelectItem>
          </SelectContent>
        </Select>
      </div>

      {/* Main Content Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Chart */}
        <Card className="lg:col-span-2 bg-card border-border">
          <CardHeader>
            <CardTitle className="text-foreground">Predicted vs. Actual Congestion Levels</CardTitle>
            <CardDescription>Last 7 Days</CardDescription>
          </CardHeader>
          <CardContent>
            <PredictionChart />
          </CardContent>
        </Card>

        {/* Metrics */}
        <div className="space-y-6">
          <MetricCard
            title="Root Mean Squared Error"
            value="0.18"
            change="0.02%"
            isPositive={false}
          />
          <MetricCard
            title="Mean Absolute Error"
            value="0.12"
            change="0.01%"
            isPositive={true}
          />
          <MetricCard
            title="R-squared (R²) Score"
            value="0.92"
            change="0.05%"
            isPositive={true}
          />
          <MetricCard
            title="Model Accuracy"
            value="95.8%"
            change="1.2%"
            isPositive={true}
          />
        </div>
      </div>

      {/* Classification Report */}
      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="text-foreground">Classification Report</CardTitle>
          <CardDescription>Precision, recall, and F1-score for each congestion level.</CardDescription>
        </CardHeader>
        <CardContent>
          <ClassificationTable />
        </CardContent>
      </Card>
    </div>
  );
};

export default ModelEvaluation;
