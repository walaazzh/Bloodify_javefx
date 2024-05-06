package models.user;

public class Category {

        private int Idcategory;
        private String name;
        private String description;

        // Constructors
        public Category() {
        }

        public Category(String name, String description) {

            this.name = name;
            this.description = description;
        }

        // Getters
        public int getIdcategory() {
            return Idcategory;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        // Setters
        public void setIdcategory(int Idcategory) {
            this.Idcategory = Idcategory;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        // toString() method
        @Override
        public String toString() {
            return "Category{" +
                    "Idcategory=" + Idcategory +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
        public Category(int Idcategory, String name, String description) {
            this.Idcategory = Idcategory;
            this.name = name;
            this.description = description;
        }
}
