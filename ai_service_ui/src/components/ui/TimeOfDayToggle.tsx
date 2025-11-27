interface TimeOfDayToggleProps {
  timeOfDay: "morning" | "evening";
  onChange: (value: "morning" | "evening") => void;
}

const TimeOfDayToggle = ({ timeOfDay, onChange }: TimeOfDayToggleProps) => {
  return (
    <div className="flex gap-2">
      <button
        className={`px-4 py-1.5 rounded-md transition ${
          timeOfDay === "morning"
            ? "bg-primary text-primary-foreground"
            : "bg-secondary text-secondary-foreground"
        }`}
        onClick={() => onChange("morning")}
      >
        아침
      </button>
      <button
        className={`px-4 py-1.5 rounded-md transition ${
          timeOfDay === "evening"
            ? "bg-primary text-primary-foreground"
            : "bg-secondary text-secondary-foreground"
        }`}
        onClick={() => onChange("evening")}
      >
        저녁
      </button>
    </div>
  );
};

export default TimeOfDayToggle;
