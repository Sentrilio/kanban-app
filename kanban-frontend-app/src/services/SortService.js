class SortService {

    comparePosition(a, b) {
        return a.position - b.position;
    }
    compareName(a, b) {
        return a.name.localeCompare(b.name);
    }
    sortColumns(columns) {
        columns.sort(this.comparePosition);
    }
    sortByName(array) {
        array.sort(this.compareName);
    }
    sortByPosition(array){
        array.sort(this.comparePosition);
    }
    // sortTasks(columns) {
    //     column.tasks.sort(this.comparePosition);
    // }

    //sorting tasks in columns
    // this.sortColumns();
    // this.columns.forEach(column => {
    // column.tasks.sort(this.compare);
    // });
}

export default new SortService();