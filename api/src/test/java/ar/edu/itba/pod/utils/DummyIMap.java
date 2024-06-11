package ar.edu.itba.pod.utils;

import com.hazelcast.aggregation.Aggregator;
import com.hazelcast.core.*;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.MapInterceptor;
import com.hazelcast.map.QueryCache;
import com.hazelcast.map.listener.MapListener;
import com.hazelcast.map.listener.MapPartitionLostListener;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.aggregation.Aggregation;
import com.hazelcast.mapreduce.aggregation.Supplier;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.projection.Projection;
import com.hazelcast.query.Predicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DummyIMap<k,v> implements IMap<k,v> {

    private Map<k, v> map = new HashMap<>();

    @Override
    public void putAll(Map<? extends k, ? extends v> map) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public v get(Object o) {
        return map.get(o);
    }

    @Override
    public v put(k k, v v) {
        return map.put(k, v);
    }

    @Override
    public v remove(Object o) {
        return null;
    }

    @Override
    public boolean remove(Object o, Object o1) {
        return false;
    }

    @Override
    public void removeAll(Predicate<k, v> predicate) {

    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public void flush() {

    }

    @Override
    public Map<k, v> getAll(Set<k> set) {
        return null;
    }

    @Override
    public void loadAll(boolean b) {

    }

    @Override
    public void loadAll(Set<k> set, boolean b) {

    }

    @Override
    public void clear() {

    }

    @Override
    public ICompletableFuture<v> getAsync(k k) {
        return null;
    }

    @Override
    public ICompletableFuture<v> putAsync(k k, v v) {
        return null;
    }

    @Override
    public ICompletableFuture<v> putAsync(k k, v v, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public ICompletableFuture<Void> setAsync(k k, v v) {
        return null;
    }

    @Override
    public ICompletableFuture<Void> setAsync(k k, v v, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public ICompletableFuture<v> removeAsync(k k) {
        return null;
    }

    @Override
    public boolean tryRemove(k k, long l, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public boolean tryPut(k k, v v, long l, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public v put(k k, v v, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public void putTransient(k k, v v, long l, TimeUnit timeUnit) {

    }

    @Override
    public v putIfAbsent(k k, v v) {
        return null;
    }

    @Override
    public v putIfAbsent(k k, v v, long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public boolean replace(k k, v v, v v1) {
        return false;
    }

    @Override
    public v replace(k k, v v) {
        return null;
    }

    @Override
    public void set(k k, v v) {

    }

    @Override
    public void set(k k, v v, long l, TimeUnit timeUnit) {

    }

    @Override
    public void lock(k k) {

    }

    @Override
    public void lock(k k, long l, TimeUnit timeUnit) {

    }

    @Override
    public boolean isLocked(k k) {
        return false;
    }

    @Override
    public boolean tryLock(k k) {
        return false;
    }

    @Override
    public boolean tryLock(k k, long l, TimeUnit timeUnit) throws InterruptedException {
        return false;
    }

    @Override
    public boolean tryLock(k k, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock(k k) {

    }

    @Override
    public void forceUnlock(k k) {

    }

    @Override
    public String addLocalEntryListener(MapListener mapListener) {
        return null;
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener) {
        return null;
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener, Predicate<k, v> predicate, boolean b) {
        return null;
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener, Predicate<k, v> predicate, boolean b) {
        return null;
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener, Predicate<k, v> predicate, k k, boolean b) {
        return null;
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener, Predicate<k, v> predicate, k k, boolean b) {
        return null;
    }

    @Override
    public String addInterceptor(MapInterceptor mapInterceptor) {
        return null;
    }

    @Override
    public void removeInterceptor(String s) {

    }

    @Override
    public String addEntryListener(MapListener mapListener, boolean b) {
        return null;
    }

    @Override
    public String addEntryListener(EntryListener entryListener, boolean b) {
        return null;
    }

    @Override
    public boolean removeEntryListener(String s) {
        return false;
    }

    @Override
    public String addPartitionLostListener(MapPartitionLostListener mapPartitionLostListener) {
        return null;
    }

    @Override
    public boolean removePartitionLostListener(String s) {
        return false;
    }

    @Override
    public String addEntryListener(MapListener mapListener, k k, boolean b) {
        return null;
    }

    @Override
    public String addEntryListener(EntryListener entryListener, k k, boolean b) {
        return null;
    }

    @Override
    public String addEntryListener(MapListener mapListener, Predicate<k, v> predicate, boolean b) {
        return null;
    }

    @Override
    public String addEntryListener(EntryListener entryListener, Predicate<k, v> predicate, boolean b) {
        return null;
    }

    @Override
    public String addEntryListener(MapListener mapListener, Predicate<k, v> predicate, k k, boolean b) {
        return null;
    }

    @Override
    public String addEntryListener(EntryListener entryListener, Predicate<k, v> predicate, k k, boolean b) {
        return null;
    }

    @Override
    public EntryView<k, v> getEntryView(k k) {
        return null;
    }

    @Override
    public boolean evict(k k) {
        return false;
    }

    @Override
    public void evictAll() {

    }

    @Override
    public Set<k> keySet() {
        return null;
    }

    @Override
    public Collection<v> values() {
        return null;
    }

    @Override
    public Set<Entry<k, v>> entrySet() {
        return null;
    }

    @Override
    public Set<k> keySet(Predicate predicate) {
        return null;
    }

    @Override
    public Set<Entry<k, v>> entrySet(Predicate predicate) {
        return null;
    }

    @Override
    public Collection<v> values(Predicate predicate) {
        return null;
    }

    @Override
    public Set<k> localKeySet() {
        return null;
    }

    @Override
    public Set<k> localKeySet(Predicate predicate) {
        return null;
    }

    @Override
    public void addIndex(String s, boolean b) {

    }

    @Override
    public LocalMapStats getLocalMapStats() {
        return null;
    }

    @Override
    public Object executeOnKey(k k, EntryProcessor entryProcessor) {
        return null;
    }

    @Override
    public Map<k, Object> executeOnKeys(Set<k> set, EntryProcessor entryProcessor) {
        return null;
    }

    @Override
    public void submitToKey(k k, EntryProcessor entryProcessor, ExecutionCallback executionCallback) {

    }

    @Override
    public ICompletableFuture submitToKey(k k, EntryProcessor entryProcessor) {
        return null;
    }

    @Override
    public Map<k, Object> executeOnEntries(EntryProcessor entryProcessor) {
        return null;
    }

    @Override
    public Map<k, Object> executeOnEntries(EntryProcessor entryProcessor, Predicate predicate) {
        return null;
    }

    @Override
    public <R> R aggregate(Aggregator<Entry<k, v>, R> aggregator) {
        return null;
    }

    @Override
    public <R> R aggregate(Aggregator<Entry<k, v>, R> aggregator, Predicate<k, v> predicate) {
        return null;
    }

    @Override
    public <R> Collection<R> project(Projection<Entry<k, v>, R> projection) {
        return null;
    }

    @Override
    public <R> Collection<R> project(Projection<Entry<k, v>, R> projection, Predicate<k, v> predicate) {
        return null;
    }

    @Override
    public <SuppliedValue, Result> Result aggregate(Supplier<k, v, SuppliedValue> supplier, Aggregation<k, SuppliedValue, Result> aggregation) {
        return null;
    }

    @Override
    public <SuppliedValue, Result> Result aggregate(Supplier<k, v, SuppliedValue> supplier, Aggregation<k, SuppliedValue, Result> aggregation, JobTracker jobTracker) {
        return null;
    }

    @Override
    public QueryCache<k, v> getQueryCache(String s) {
        return null;
    }

    @Override
    public QueryCache<k, v> getQueryCache(String s, Predicate<k, v> predicate, boolean b) {
        return null;
    }

    @Override
    public QueryCache<k, v> getQueryCache(String s, MapListener mapListener, Predicate<k, v> predicate, boolean b) {
        return null;
    }

    @Override
    public String getPartitionKey() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getServiceName() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
