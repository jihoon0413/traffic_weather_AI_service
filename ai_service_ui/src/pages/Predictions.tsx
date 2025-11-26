import { useState } from 'react';
import { TrafficMap } from '@/components/TrafficMap';
import { CongestionForecastPanel } from '@/components/CongestionForecastPanel';

const Predictions = () => {
  const [selectedRoute, setSelectedRoute] = useState<{
    name: string;
    segment: string;
    direction: string;
  }>();

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold text-foreground mb-2">Predictions</h1>
        <p className="text-muted-foreground">Real-time traffic congestion forecasting and route analysis.</p>
      </div>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-[calc(100vh-200px)]">
        {/* Map Section */}
        <div className="lg:col-span-2 h-full">
          <TrafficMap />
        </div>

        {/* Forecast Panel */}
        <div className="h-full overflow-y-auto">
          <CongestionForecastPanel selectedRoute={selectedRoute} />
        </div>
      </div>
    </div>
  );
};

export default Predictions;
