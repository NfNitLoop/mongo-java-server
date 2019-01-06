package de.bwaldvogel.mongo.backend;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class LinkedTreeSet<E> extends AbstractSet<E> {

    private final Set<E> elements = new TreeSet<>(new ValueComparator());
    private final List<E> orderedElements = new ArrayList<>();

    @Override
    public boolean add(E e) {
        if (elements.add(e)) {
            orderedElements.add(e);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (!elements.remove(o)) {
            return false;
        }
        removeFromOrderedElements(o);
        return true;
    }

    private void removeFromOrderedElements(Object o) {
        orderedElements.removeIf(value -> ValueComparator.compareValues(value, o) == 0);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private final Iterator<E> delegate = orderedElements.iterator();
            private E currentElement;

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public E next() {
                currentElement = delegate.next();
                return currentElement;
            }

            @Override
            public void remove() {
                delegate.remove();
                removeFromOrderedElements(currentElement);
            }
        };
    }

    @Override
    public int hashCode() {
        return elements.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof LinkedTreeSet)) {
            return false;
        }
        LinkedTreeSet<?> otherSet = (LinkedTreeSet<?>) o;
        return orderedElements.equals(otherSet.orderedElements);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public String toString() {
        return orderedElements.toString();
    }
}
