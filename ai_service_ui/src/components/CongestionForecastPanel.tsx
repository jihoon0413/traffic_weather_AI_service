import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Cloud, Droplets, Wind } from 'lucide-react';

interface CongestionForecastPanelProps {
  selectedRoute?: {
    name: string;
    segment: string;
    direction: string;
  };
}

export const CongestionForecastPanel = ({ selectedRoute }: CongestionForecastPanelProps) => {
  return (
    <div className="space-y-6">
      {/* Congestion Forecast */}
      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="text-lg">Congestion Forecast</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <p className="text-sm text-muted-foreground mb-2">Predicted Level</p>
            <div className="flex items-baseline gap-2">
              <div className="w-3 h-3 rounded-full bg-success animate-pulse" />
              <span className="text-5xl font-bold text-success">LOW</span>
            </div>
          </div>
          <div>
            <p className="text-sm text-muted-foreground">Predicted Delay</p>
            <p className="text-lg font-semibold text-foreground">+3-5 mins</p>
          </div>
        </CardContent>
      </Card>

      {/* Selection Information */}
      {selectedRoute ? (
        <Card className="bg-card border-border">
          <CardHeader>
            <CardTitle className="text-lg">Selection Information</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex justify-between">
              <span className="text-sm text-muted-foreground">Route Name</span>
              <span className="text-sm font-medium text-foreground">{selectedRoute.name}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-sm text-muted-foreground">Selected Segment</span>
              <span className="text-sm font-medium text-foreground">{selectedRoute.segment}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-sm text-muted-foreground">Direction</span>
              <span className="text-sm font-medium text-foreground">{selectedRoute.direction}</span>
            </div>
          </CardContent>
        </Card>
      ) : null}

      {/* Current Conditions */}
      <Card className="bg-card border-border">
        <CardHeader>
          <CardTitle className="text-lg">Current Conditions</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex justify-around items-center">
            <div className="flex flex-col items-center gap-2">
              <Cloud className="h-8 w-8 text-primary" />
              <span className="text-2xl font-bold text-foreground">18°C</span>
            </div>
            <div className="flex flex-col items-center gap-2">
              <Droplets className="h-8 w-8 text-primary" />
              <span className="text-2xl font-bold text-foreground">10%</span>
            </div>
            <div className="flex flex-col items-center gap-2">
              <Wind className="h-8 w-8 text-primary" />
              <span className="text-2xl font-bold text-foreground">15 km/h</span>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};
