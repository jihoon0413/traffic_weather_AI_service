import { LayoutDashboard, PieChart, TrendingUp, Database, Settings, LogOut } from "lucide-react";
import { NavLink } from "./NavLink";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";

const navItems = [
  { to: "/predictions", icon: TrendingUp, label: "Predictions" },
  { to: "/model-evaluation", icon: PieChart, label: "Model Evaluation" },
  { to: "/", icon: LayoutDashboard, label: "Dashboard" },
  { to: "/datasets", icon: Database, label: "Datasets" },
];

export const Sidebar = () => {
  return (
    <aside className="w-64 bg-sidebar border-r border-sidebar-border flex flex-col">
      {/* User Profile */}
      <div className="p-6 border-b border-sidebar-border">
        <div className="flex items-center gap-3">
          <Avatar className="h-10 w-10">
            <AvatarFallback className="bg-primary text-primary-foreground">ER</AvatarFallback>
          </Avatar>
          <div>
            <h3 className="font-semibold text-sm text-foreground">Dr. Evelyn Reed</h3>
            <p className="text-xs text-muted-foreground">Data Scientist</p>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-4">
        <ul className="space-y-2">
          {navItems.map((item) => (
            <li key={item.to}>
              <NavLink
                to={item.to}
                className="flex items-center gap-3 px-4 py-3 rounded-lg text-sidebar-foreground hover:bg-sidebar-accent transition-colors"
                activeClassName="bg-primary/10 text-primary hover:bg-primary/15"
              >
                <item.icon className="h-5 w-5" />
                <span className="text-sm font-medium">{item.label}</span>
              </NavLink>
            </li>
          ))}
        </ul>
      </nav>

      {/* Bottom Actions */}
      <div className="p-4 border-t border-sidebar-border space-y-2">
        <button className="flex items-center gap-3 px-4 py-3 rounded-lg text-sidebar-foreground hover:bg-sidebar-accent transition-colors w-full">
          <Settings className="h-5 w-5" />
          <span className="text-sm font-medium">Settings</span>
        </button>
        <button className="flex items-center gap-3 px-4 py-3 rounded-lg text-sidebar-foreground hover:bg-sidebar-accent transition-colors w-full">
          <LogOut className="h-5 w-5" />
          <span className="text-sm font-medium">Logout</span>
        </button>
      </div>
    </aside>
  );
};
