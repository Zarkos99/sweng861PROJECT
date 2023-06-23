package sweng861;

/**
 * This class is a hack to get around difficulties with packaging modular javafx
 * projects to get all module_info.class dependencies communicating with each
 * other effectively. This took quite a bit of effort to find and (partially)
 * understand
 */
public class LauncherMain {
  public static void main(String[] args) { App.main(args); }
}
