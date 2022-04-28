package net.boster.particles.main.gui.multipage;

public enum MultiPageButtonAppearance {

    NEEDED {
        @Override
        public boolean checkAppear(MultiPageGUI gui, boolean next) {
            if(next) {
                return gui.getPageNumber() < gui.getPages();
            } else {
                return gui.getPageNumber() > 1;
            }
        }
    },
    ALWAYS {
        @Override
        public boolean checkAppear(MultiPageGUI gui, boolean next) {
            return true;
        }
    };

    public abstract boolean checkAppear(MultiPageGUI gui, boolean next);
}
