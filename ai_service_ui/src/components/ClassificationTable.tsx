import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";

const data = [
  { level: "Low", precision: "0.98", recall: "0.99", f1Score: "0.98", support: "1520" },
  { level: "Medium", precision: "0.91", recall: "0.93", f1Score: "0.92", support: "850" },
  { level: "High", precision: "0.89", recall: "0.87", f1Score: "0.88", support: "315" },
  { level: "Avg / Total", precision: "0.96", recall: "0.96", f1Score: "0.96", support: "2685" },
];

export const ClassificationTable = () => {
  return (
    <div className="rounded-lg border border-border overflow-hidden">
      <Table>
        <TableHeader>
          <TableRow className="border-border hover:bg-transparent">
            <TableHead className="text-muted-foreground font-semibold uppercase text-xs">Congestion Level</TableHead>
            <TableHead className="text-muted-foreground font-semibold uppercase text-xs">Precision</TableHead>
            <TableHead className="text-muted-foreground font-semibold uppercase text-xs">Recall</TableHead>
            <TableHead className="text-muted-foreground font-semibold uppercase text-xs">F1-Score</TableHead>
            <TableHead className="text-muted-foreground font-semibold uppercase text-xs">Support</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {data.map((row, index) => (
            <TableRow 
              key={row.level} 
              className={`border-border ${index === data.length - 1 ? "border-t-2 font-semibold" : ""}`}
            >
              <TableCell className="text-foreground">{row.level}</TableCell>
              <TableCell className="text-foreground">{row.precision}</TableCell>
              <TableCell className="text-foreground">{row.recall}</TableCell>
              <TableCell className="text-foreground">{row.f1Score}</TableCell>
              <TableCell className="text-foreground">{row.support}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};
