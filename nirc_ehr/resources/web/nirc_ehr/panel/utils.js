

splitIds = function(subjectArray, unsorted, preserveDuplicates){
    if (!subjectArray){
        return [];
    }

    subjectArray = Ext4.String.trim(subjectArray);
    subjectArray = subjectArray.replace(/[\s,;]+/g, ';');
    subjectArray = subjectArray.replace(/(^;|;$)/g, '');

    if (subjectArray){
        subjectArray = subjectArray.split(';');
    }
    else {
        subjectArray = [];
    }

    if (subjectArray.length > 0) {
        if (!preserveDuplicates) {
            subjectArray = Ext4.unique(subjectArray);
        }
        if (!unsorted) {
            subjectArray.sort();
        }
    }

    return subjectArray;
}

singleSubjectLoadReport = function (tab, callback, panel, forceRefresh){
    var subjectArray = splitIds(this.down('#subjArea').getValue());  // Don't lowercase the subject Ids

    if (subjectArray.length > 0){
        subjectArray = Ext4.unique(subjectArray);
    }

    this.subjects = subjectArray;
    this.aliases = {};
    if (Ext4.isDefined(this.aliasTable)) {
        this.getAlias(subjectArray, callback, panel, tab, forceRefresh);
    }
    else {
        callback.call(panel, this.handleFilters(tab, this.subjects), forceRefresh);
    }
}