import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Calendar } from "@/components/ui/calendar";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Cloud, Droplets, Snowflake, CalendarIcon, ChevronLeft, ChevronRight } from "lucide-react";
import { useEffect, useState } from "react";
import { format, set } from "date-fns";
import { ko } from "date-fns/locale";
import TimeOfDayToggle from "./ui/timeOfDayToggle";
import axios from "axios";

interface CongestionForecastPanelProps {busStopData: {
    name: string;
    lat: number;
    lng: number;
    busStopId: number
  } | null;}

export const CongestionForecastPanel = ({ busStopData }: CongestionForecastPanelProps) => {
  const [timeOfDay, setTimeOfDay] = useState("morning");
  const [selectedDate, setSelectedDate] = useState<Date>(new Date());
  const [isCalendarOpen, setIsCalendarOpen] = useState(false);
  const [congestion, setCongestion] = useState(null);
  const [weatherInfo, setWeatherInfo] = useState<{
    date: string | null;
    temp: number | null;
    prec: number | null;
    snow: number | null;
  } | null>(null);

  useEffect(() => {
    if(!busStopData) {
      setWeatherInfo(null);
      return;
    }

    const fetchWeatherInfo = async () => {
      try {
        const yyyy = selectedDate.getFullYear();
        const mm = String(selectedDate.getMonth() + 1).padStart(2, "0");
        const dd = String(selectedDate.getDate()).padStart(2, "0");

        const formattedDate = `${yyyy}${mm}${dd}`;

        const params = new URLSearchParams({
          date: formattedDate,
          lat: String(busStopData.lat),
          lng:String(busStopData.lng),
          time: timeOfDay.toUpperCase()
        })
        
     
        const res = await axios.get(`http://localhost:8080/api/weather?${params.toString()}`);
        const data = res.data;
        console.log(data);
        setWeatherInfo(data)
      } catch(err) {
        console.error("Weathr API Error");
        setWeatherInfo(null);
      }
    }

    fetchWeatherInfo();
  }, [busStopData, timeOfDay, selectedDate])

  const handlePreviousDay = () => {
    const newDate = new Date(selectedDate);
    newDate.setDate(newDate.getDate() - 1);
    setSelectedDate(newDate);
  };

  const handleNextDay = () => {
    const newDate = new Date(selectedDate);
    newDate.setDate(newDate.getDate() + 1);
    setSelectedDate(newDate);
  };

  const handleDateSelect = (date: Date | undefined) => {
    if (date) {
      setSelectedDate(date);
      setIsCalendarOpen(false);
    }
  };

  const predictCongestion = async () => {
    console.log(busStopData.busStopId)
  }

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
              {/* <div className="w-3 h-3 rounded-full bg-success animate-pulse" /> */}
              <span className="text-5xl font-bold text-success">{congestion?? "-"}</span>
            </div>
          </div>
          {/* <div>
            <p className="text-sm text-muted-foreground">Predicted Delay</p>
            <p className="text-lg font-semibold text-foreground">+3-5 mins</p>
          </div> */}
        </CardContent>
      </Card>

      {/* Current Conditions */}
      <Card className="bg-card border-border">
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg">Current Conditions</CardTitle>
          <div className="flex items-center gap-2 bg-secondary rounded-lg px-3 py-1.5">
            <button
              onClick={handlePreviousDay}
              className="hover:bg-muted rounded p-1 transition"
            >
              <ChevronLeft className="h-4 w-4" />
            </button>
            
            <Popover open={isCalendarOpen} onOpenChange={setIsCalendarOpen}>
              <PopoverTrigger asChild>
                <button className="flex items-center gap-2 text-sm font-medium min-w-[140px] justify-center hover:text-primary transition">
                  <CalendarIcon className="h-4 w-4" />
                  {format(selectedDate, "M월 d일 (EEE)", { locale: ko })}
                </button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="end">
                <Calendar
                  mode="single"
                  selected={selectedDate}
                  onSelect={handleDateSelect}
                  initialFocus
                  className="pointer-events-auto"
                />
              </PopoverContent>
            </Popover>

            <button
              onClick={handleNextDay}
              className="hover:bg-muted rounded p-1 transition"
            >
              <ChevronRight className="h-4 w-4" />
            </button>
          </div>
        </div>
        <br/>
        <div className="flex items-center justify-between">
            <span>{busStopData?.name ?? "정류장을 선택해주세요"}</span>
            <TimeOfDayToggle 
              timeOfDay={timeOfDay as "morning" | "evening"}
              onChange={(value) => setTimeOfDay(value)}
            />
        </div>
      </CardHeader>

      <CardContent>
        <div className="flex justify-around items-center">
          <div className="flex flex-col items-center gap-2">
            <Cloud className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">{weatherInfo?.temp ?? "-"}°C</span>
          </div>
          <div className="flex flex-col items-center gap-2">
            <Droplets className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">{weatherInfo?.prec ?? "-"}mm</span>
          </div>
          <div className="flex flex-col items-center gap-2">
            <Snowflake className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">{weatherInfo?.snow ?? "-"}mm</span>
          </div>
        </div>

        <div className="flex justify-center mt-6">
          <button
            className="px-6 py-2 rounded-md bg-primary text-primary-foreground hover:bg-primary/80 transition"
          onClick={predictCongestion}>
            Search
          </button>
        </div>
      </CardContent>
    </Card>

    
    </div>
  );
};
